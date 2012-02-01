package com.soletta.seek.util.args;

import static java.util.Collections.sort;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Uses introspection to figure out the command line switches and arguments. Introspection will be performed on the
 * object. The read methods for the properties will be checked for annotations, if annotations are necessary to
 * customize the behavior. Strings are converted into the right type of object for the bean's setters. Additional
 * parsing methods can be specified through parseClass and parseMethod. Ordered, unnamed position arguments can be
 * specified, as can a single arguments that takes the unnamed values.
 * <p>
 * In the example below a configuration object is declared as a JavaBean; @Arg is used in a few places to customize the
 * behavior.
 * 
 * <pre>
 * {@code public void simpleConfig() throws ArgException, UnknownHostException {
 *     SimpleConfig sc = new SimpleConfig();
 *     Set<String> processed = LibArgs.processArguments(sc, "-name", "ross", "-depth", "12", "-v", "-addr", "192.168.1.1", "-toast=bread", "file1.txt", "file2.txt");
 * 
 *     assertEquals("ross", sc.getName());
 *     assertEquals(12, sc.getDepth());
 *     assertEquals(true, sc.isVerbose());
 *     assertEquals("bread", sc.getToast());
 *     assertEquals(InetAddress.getByName("192.168.1.1"), sc.getAddr());
 *     assertEquals(Arrays.asList("file1.txt", "file2.txt"), sc.getFiles());
 *     
 *     assertEquals(6, processed.size());
 * }
 * 
 * 
 * public static class SimpleConfig {
 *     
 *     private String name;
 *     private int depth;
 *     private boolean verbose;
 *     private String input;
 *     private InetAddress addr;
 *     private String toast;
 *     
 *     private List<String> files = new ArrayList<String>();
 *     
 *     public String getName() {
 *         return name;
 *     }
 *     public void setName(String name) {
 *         this.name = name;
 *     }
 *     public int getDepth() {
 *         return depth;
 *     }
 *     public void setDepth(int depth) {
 *         this.depth = depth;
 *     }
 *     
 *     // switches are booleans; the presence of the switch results in the boolean 
 *     // property being set to true.
 *     public boolean isVerbose() {
 *         return verbose;
 *     }
 *     public void setVerbose(boolean verbose) {
 *         this.verbose = verbose;
 *     }
 * 
 *     // Tells LibArgs which static method to use, to get an InetAddress from the string.
 *     &#064;Arg(parseClass=InetAddress.class, parseMethod="getByName")
 *     public InetAddress getAddr() {
 *         return addr;
 *     }
 *     public void setAddr(InetAddress addr) {
 *         this.addr = addr;
 *     }
 *     
 *     // Flags this argument as required, at position 0
 *     &#064;Arg(pos=0)
 *     public String getInput() {
 *         return input;
 *     }
 *     public void setInput(String input) {
 *         this.input = input;
 *     }
 *     // Indicates this argument accepts the remaining unnamed parameters
 *     // Each parameter is put into the list.
 *     // Datatype conversion is done on the list, so it doesn't have to be
 *     // strings.
 *     &#064;Arg(remaining = true)
 *     public List<String> getFiles() {
 *         return files;
 *     }
 *     public void setFiles(List<String> files) {
 *         this.files = files;
 *     }
 *     public String getToast() {
 *         return toast;
 *     }
 *     public void setToast(String toast) {
 *         this.toast = toast;
 *     }
 * 
 * }
 * </pre>
 * 
 * See the documentation for more details.
 * 
 * @author rjudson
 * @version $Revision: 1.0 $
 */
public class LibArgs {

    final static Logger log = Logger.getLogger(LibArgs.class.getName());

    final static Map<String, Parser> KNOWN_PARSERS = new HashMap<String, Parser>();

    static {
        new StaticParser("java.awt.Color", "decode", "getColor");
    }

    /**
     * Create a LibArgs object and process the given arguments with it. This is a convenient method to use if you do not
     * need to customize any of LibArgs' handling of types.
     * 
     * @param config
     * @param args
     * @return Set<String>
     * @throws ArgException
     */
    public static Set<String> process(Object config, String... args) throws ArgException {
        return new LibArgs().processArguments(config, args);
    }

    /**
     * Examines the config object using introspection, then sets its properties based on the arguments that are passed
     * in.
     * 
     * @param config
     * @param args
     * @return A set with the names of the arguments that were processed. * @throws ArgException
     */
    public Set<String> processArguments(Object config, String... args) throws ArgException {

        assert config != null;
        assert args != null;

        LinkedHashSet<String> ret = new LinkedHashSet<String>();

        try {

            ParameterInfo parameters = introspect(config);

            int positionalIndex = 0;
            int argIndex = 0;

            while (argIndex < args.length) {
                String a = args[argIndex++];
                String longName;

                if (a.startsWith("-")) {
                    longName = a.startsWith("--") ? a.substring(2) : a.substring(1);
                    String value;
                    // is there an embedded '='? if so, it's an all-in-one set.
                    int pos = longName.indexOf('=');
                    // Either take the embedded value, or grab the next
                    // argument.
                    if (pos >= 0) {
                        value = longName.substring(pos + 1);
                        longName = longName.substring(0, pos);
                    } else {
                        value = null;
                        // value = args[i++];
                    }

                    boolean processed = false;
                    for (Parameter p : parameters)
                        if (p.named()
                                && (p.aliased(longName) || longName.equalsIgnoreCase(p.longName) || (longName.length() == 1 && longName
                                        .charAt(0) == p.shortName))) {
                            if (value == null && p.type != ArgType.SWITCH) {
                                if (argIndex >= args.length)
                                    throw new ArgException("Missing argment after " + a);
                                value = args[argIndex++];
                            }
                            processed = true;
                            storeValue(config, p, longName, value);
                            ret.add(longName);
                            break;
                        }

                    if (!processed) {
                        throw new ArgException("Unknown argument: " + a);
                    }
                } else {
                    boolean processed = false;
                    // Use the next arg index, or go to "remaining".
                    for (Parameter p : parameters) {
                        if (!p.named() && p.index == positionalIndex) {
                            storeValue(config, p, p.longName, a);
                            processed = true;
                            positionalIndex++;
                            ret.add(p.longName);
                            break;
                        }
                    }

                    if (!processed) {
                        for (Parameter p : parameters) {
                            if (p.type == ArgType.REMAINING) {
                                storeValue(config, p, p.longName, a);
                                ret.add(p.longName);
                                break;
                            }
                        }
                    }
                }
            }

            // Check to see if any of the required arguments were not specified.
            for (Parameter p : parameters)
                if (p.type == ArgType.ARG || (p.arg != null && p.arg.required() && !p.used))
                    throw new ArgException("Argument " + p.longName + " is required, and was not specified");

        } catch (IntrospectionException e) {
            // This should pretty much never happen, so we'll just convert it
            // over into
            // a runtime exception.
            throw new RuntimeException("Can't introspect configuration object", e);
        }

        return ret;
    }

    /**
     * Method introspect.
     * 
     * @param config
     *            Object
     * @return ParameterInfo
     * @throws IntrospectionException
     * @throws ArgException
     */
    static ParameterInfo introspect(Object config) throws IntrospectionException, ArgException {
        ParameterInfo parameters = new ParameterInfo();
        Set<Character> shortNames = new HashSet<Character>();

        Map<String, Arg> classArgs = new LinkedHashMap<String, Arg>();
        Args args = config.getClass().getAnnotation(Args.class);
        if (args != null)
            for (Arg arg : args.value())
                classArgs.put(arg.name(), arg);

        BeanInfo bi = Introspector.getBeanInfo(config.getClass(), Object.class);
        for (PropertyDescriptor pd : bi.getPropertyDescriptors()) {
            if (pd.getReadMethod() != null) {
                Arg arg = pd.getReadMethod().getAnnotation(Arg.class);
                if (arg == null)
                    arg = classArgs.get(pd.getName());
                if (arg != null && arg.single() != 0)
                    if (!shortNames.add(arg.single())) {
                        throw new ArgException("Can't assign single character " + arg.single() + " to more than one property.");
                    }
            }
        }

        for (PropertyDescriptor pd : bi.getPropertyDescriptors()) {
            Parameter param = new Parameter();
            param.longName = pd.getName();
            if (pd.getWriteMethod() != null) {
                param.pd = pd;
                Method readMethod = pd.getReadMethod();
                Arg arg = readMethod.getAnnotation(Arg.class);
                if (arg == null)
                    arg = classArgs.get(pd.getName());
                param.arg = arg;
                if (arg != null) {
                    if (arg.name().length() > 0)
                        param.longName = arg.name();
                    if (arg.single() != 0)
                        param.shortName = arg.single();
                }
                if (readMethod != null) {
                    log.fine("Checking " + param.longName + " for annotations.");
                    if (param.shortName == 0 && shortNames.add(param.longName.charAt(0))) {
                        param.shortName = param.longName.charAt(0);
                        log.fine("Assigning char " + param.shortName + " to " + param.longName);
                    }
                    if (pd.getPropertyType() == Boolean.TYPE || pd.getPropertyType() == Boolean.class) {
                        param.type = ArgType.SWITCH;
                    } else if (arg != null && (arg.pos() >= 0 || arg.remaining())) {
                        param.type = arg.remaining() ? ArgType.REMAINING : ArgType.FLAG;
                        param.index = arg.pos();
                    } else {
                        param.type = ArgType.FLAG;
                    }
                }

                if (param != null) {
                    assert param.type != null;
                    assert param.longName != null;
                    parameters.add(param);
                }

            } else {
                log.fine("Skipping " + pd.getName() + " because it isn't writeable.");
            }
        }
        return parameters;
    }

    /**
     */
    @SuppressWarnings("serial")
    static class ParameterInfo extends ArrayList<Parameter> {

    }

    /**
     * Method storeValue.
     * 
     * @param config
     *            Object
     * @param p
     *            Parameter
     * @param longName
     *            String
     * @param value
     *            String
     */
    private void storeValue(Object config, Parameter p, String longName, String value) {
        p.used = true;
        try {
            Object v = value;
            if (value != null) {
                Class<?> propType = p.pd.getPropertyType();

                if (List.class.isAssignableFrom(propType)) {

                    Class<?> vtype = String.class;
                    Method rm = p.pd.getReadMethod();
                    Type rt = rm.getGenericReturnType();
                    if (rt instanceof ParameterizedType) {
                        vtype = (Class<?>) ((ParameterizedType) rt).getActualTypeArguments()[0];
                    }

                    v = convertFromString(p.arg, vtype, value);

                    @SuppressWarnings("unchecked")
                    List<Object> lst = (List<Object>) rm.invoke(config);
                    if (lst == null) {
                        lst = new ArrayList<Object>();
                        lst.add(v);
                        v = lst;
                    } else {
                        lst.add(v);
                        v = null;
                    }
                } else {
                    v = convertFromString(p.arg, propType, value);
                }
            }

            if (p.type == ArgType.SWITCH)
                v = true;
            if (v != null) {
                try {
                    p.pd.getWriteMethod().invoke(config, v);
                } catch (Exception e) {
                    throw new ArgException("Argument " + v + " was not converted to acceptable type, to call "
                            + p.pd.getWriteMethod().getName());
                }
            }

        } catch (Exception e) {
            log.log(Level.WARNING, "Unable to set argument", e);
        }

    }

    /**
     * Method convertFromString.
     * 
     * @param arg
     *            Arg
     * @param propType
     *            Class<?>
     * @param value
     *            String
     * @return Object
     * @throws Exception
     */
    protected Object convertFromString(Arg arg, Class<?> propType, String value) throws Exception {
        Object v = value;
        if (propType == Boolean.TYPE || propType == Boolean.class) {
            v = Boolean.valueOf(value);
        } else if (propType == Byte.TYPE || propType == Byte.class) {
            v = Byte.valueOf(value);
        } else if (propType == Character.TYPE || propType == Character.class) {
            v = value.charAt(0);
        } else if (propType == Short.TYPE || propType == Short.class) {
            v = Short.valueOf(value);
        } else if (propType == Integer.TYPE || propType == Integer.class) {
            v = Integer.valueOf(value);
        } else if (propType == Long.TYPE || propType == Long.class) {
            v = Long.valueOf(value);
        } else if (propType == Float.TYPE || propType == Float.class) {
            v = Float.valueOf(value);
        } else if (propType == Double.TYPE || propType == Double.class) {
            v = Double.valueOf(value);
        } else if (propType.isEnum()) {

            Enum<?>[] constants = (Enum<?>[]) propType.getEnumConstants();
            String upper = value.toUpperCase();
            for (Enum<?> e : constants)
                if (e.name().equals(upper))
                    return e;

        } else if (propType != String.class) {
            Method parseMethod;
            if (arg != null && arg.parseMethod().length() > 0) {
                parseMethod = methodOrNull(propType, arg.parseMethod(), String.class);
                if (parseMethod != null) {
                    v = parseMethod.invoke(null, value);
                } else {
                    throw new ArgException("Parse method " + arg.parseMethod() + " does not exist");
                }
            } else {

                Parser parser = KNOWN_PARSERS.get(propType.getName());
                if (parser != null) {
                    try {
                        v = parser.parse(value);
                    } catch (Exception ex) {
                        throw new ArgException("Can't parse " + propType.getName() + " from " + value);
                    }
                } else {
                    parseMethod = methodOrNull(propType, "valueOf", String.class);
                    if (parseMethod == null)
                        parseMethod = methodOrNull(propType, "parse", String.class);
                    if (parseMethod != null) {
                        v = parseMethod.invoke(null, value);
                    } else {
                        // See if there's a constructor that takes a String.
                        try {
                            v = propType.getConstructor(String.class).newInstance(value);
                        } catch (Exception e) {
                            // At this point we don't know how to make one of
                            // these.
                            log.log(Level.WARNING, "Unable to construct or parse " + propType.getName() + " from a string.", e);
                        }
                    }
                }
            }
        }

        return v;
    }

    /**
     * Method methodOrNull.
     * 
     * @param cls
     *            Class<?>
     * @param methodName
     *            String
     * @param args
     *            Class<?>[]
     * @return Method
     */
    private static Method methodOrNull(Class<?> cls, String methodName, Class<?>... args) {
        try {
            return cls.getMethod(methodName, args);
        } catch (Exception ex) {
            return null;
        }
    }

    /**
     * Builds a string describing the usage of the given configuration object, based on the introspection of its
     * parameters.
     * 
     * @param config
     *            Object
     * @return String
     */
    public static String usage(Object config) {
        StringBuilder sb = new StringBuilder();

        List<Parameter> parameters;
        try {
            parameters = introspect(config);
            Usage usage = config.getClass().getAnnotation(Usage.class);

            if (usage != null)
                if (usage.value().length() > 0)
                    sb.append(usage.value()).append('\n');
            // Order the parameters by type, and then by name.
            sort(parameters);

            for (Parameter p : parameters) {
                if (!p.isHidden()) {
                    String.format("%30s %s", p.longName, p.description());
                }
            }

            if (usage != null)
                if (usage.footer().length() > 0)
                    sb.append(usage.footer()).append('\n');
            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException("Unable to introspect configuration", e);
        }

    }

    /**
     * Used to keep information about parameters.
     * 
     * @author rjudson
     * @version $Revision: 1.0 $
     */
    static class Parameter implements Comparable<Parameter> {
        ArgType type;
        PropertyDescriptor pd;
        Arg arg;
        String longName;
        char shortName;
        int index = -1;
        boolean used;

        /**
         * Method named.
         * 
         * @return boolean
         */
        boolean named() {
            return type == ArgType.FLAG || type == ArgType.SWITCH;
        }

        /**
         * Method description.
         * 
         * @return String
         */
        String description() {
            if (arg != null && arg.description().length() > 0)
                return arg.description();
            return pd.getPropertyType().getSimpleName();
        }

        /**
         * Method isHidden.
         * 
         * @return boolean
         */
        boolean isHidden() {
            return arg != null && arg.hide();
        }

        /**
         * Method aliased.
         * 
         * @param name
         *            String
         * @return boolean
         */
        boolean aliased(String name) {
            if (arg != null)
                for (String a : arg.aliases())
                    if (a.equalsIgnoreCase(name))
                        return true;
            return false;
        }

        /**
         * Method isRequired.
         * 
         * @return boolean
         */
        boolean isRequired() {
            return arg != null && arg.required();
        }

        /**
         * Method compareTo.
         * 
         * @param p2
         *            Parameter
         * @return int
         */
        @Override
        public int compareTo(Parameter p2) {
            if (type != p2.type) {
                return type.compareTo(p2.type);
            } else if (type == ArgType.ARG) {
                return index < p2.index ? -1 : index == p2.index ? 0 : 1;
            } else if (type == ArgType.REMAINING) {
                return 0;
            } else {
                return longName.compareTo(p2.longName);
            }
        }

        /**
         * Method intMin.
         * 
         * @return int
         */
        public int intMin() {
            return Integer.MIN_VALUE;
        }

        /**
         * Method intMax.
         * 
         * @return int
         */
        public int intMax() {
            return Integer.MAX_VALUE;
        }

        /**
         * Method intDefault.
         * 
         * @return int
         */
        public int intDefault() {
            return 0;
        }
    }

    /**
     */
    interface Parser {
        /**
         * Method parse.
         * 
         * @param s
         *            String
         * @return Object
         */
        Object parse(String s);
    }

    /**
     */
    abstract static class AbstractParser implements Parser {
        protected final String className;
        protected Class<?> clazz;

        /**
         * Constructor for AbstractParser.
         * 
         * @param className
         *            String
         */
        protected AbstractParser(String className) {
            this.className = className;
            assert className != null;
            assert className.length() > 0;
            KNOWN_PARSERS.put(className, this);
        }

        protected void ensureClass() {
            if (clazz == null)
                try {
                    clazz = Thread.currentThread().getContextClassLoader().loadClass(className);
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
        }

        /**
         * Method parse.
         * 
         * @param s
         *            String
         * @return Object
         * @see com.soletta.seek.util.args.LibArgs$Parser#parse(String)
         */
        public final Object parse(String s) {
            ensureClass();
            return doParse(s);
        }

        /**
         * Method doParse.
         * 
         * @param s
         *            String
         * @return Object
         */
        abstract protected Object doParse(String s);

    }

    /**
     */
    static class StaticParser extends AbstractParser {

        private final String[] methods;

        /**
         * Constructor for StaticParser.
         * 
         * @param className
         *            String
         * @param methods
         *            String[]
         */
        StaticParser(String className, String... methods) {
            super(className);
            this.methods = methods;

            assert methods != null;
            assert methods.length > 0;

            KNOWN_PARSERS.put(className, this);
        }

        /**
         * Method doParse.
         * 
         * @param s
         *            String
         * @return Object
         */
        @Override
        public Object doParse(String s) {

            for (String m : methods) {
                try {
                    Method meth = clazz.getDeclaredMethod(m, String.class);
                    return meth.invoke(null, s);
                } catch (Exception e) {
                    // Move on.
                }

            }

            return null;
        }

    }

    /**
     * Given some kind of options object, convert it into a series command line arguments. This is the inverse of the
     * process method. This is useful when you want to invoke an object in another process, but want to configure that
     * object by settings parameters on an options object, rather than assembling a command line. How can defaults be
     * detected?
     * 
     * @param options
     * @return String[]
     * @throws ArgException
     *             * @throws IntrospectionException
     */
    public static String[] toArgs(Object options) throws ArgException {
        try {
            Object optionDefault;
            try {
                optionDefault = options.getClass().newInstance();
            } catch (Exception e) {
                optionDefault = null;
            }

            List<Parameter> parameters = introspect(options);
            sort(parameters);

            List<String> args = new ArrayList<String>();

            for (ArgType type : ArgType.values()) {
                for (Parameter p : parameters) {
                    if (p.type == type) {
                        // Create an appropriate argument for each parameter.
                        try {
                            Object pValue = p.pd.getReadMethod().invoke(options);
                            Object pDefault = optionDefault == null ? null : p.pd.getReadMethod().invoke(optionDefault);

                            if (pValue == pDefault || pValue == null) {
                                // no need to write it.
                            } else if (pDefault == null || !pDefault.equals(pValue)) {
                                type.appendTo(args, p, pValue);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                }
            }

            return args.toArray(new String[args.size()]);
        } catch (IntrospectionException e) {
            throw new ArgException("Can't introspect " + options.getClass().getName());
        }
    }

}
