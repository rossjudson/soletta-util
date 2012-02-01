package com.soletta.seek.util.args;

import java.util.List;

import com.soletta.seek.util.args.LibArgs.Parameter;

/**
 * Lists the kinds of arguments.
 * 
 * @author rjudson
 * @version $Revision: 1.0 $
 */
enum ArgType {
    /**
     * A argument that, if present, sets a corresponding boolean to true.
     */
    SWITCH {
        @Override
        void appendTo(List<String> args, Parameter p, Object parameterValue) {
            args.add("--" + p.longName);
        }
    },
    /**
     * A named argument that tries to set a value.
     */
    FLAG {
        @Override
        void appendTo(List<String> args, Parameter p, Object parameterValue) {
            args.add("--" + p.longName);
            args.add(parameterValue.toString());
        }
    },
    /**
     * An unnamed argument that gets the specified position. These are required.
     */
    ARG {
        @Override
        void appendTo(List<String> args, Parameter p, Object parameterValue) {
            args.add(parameterValue.toString());
        }
    },
    /**
     * Gets the remaining unnamed arguments.
     */
    REMAINING {
        @Override
        void appendTo(List<String> args, Parameter p, Object parameterValue) {
            if (parameterValue instanceof Iterable<?>) {
                for (Object v : ((Iterable<?>) parameterValue))
                    args.add(v.toString());
            }
        }
    };

    /**
     * Method appendTo.
     * 
     * @param args
     *            List<String>
     * @param p
     *            LibArgs.Parameter
     * @param parameterValue
     *            Object
     */
    abstract void appendTo(List<String> args, LibArgs.Parameter p, Object parameterValue);
}