package com.soletta.seek.util;

import static java.util.Arrays.asList;

import java.util.Collection;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;

/**
 * Static utility functions that help deal with sets.
 * 
 * @author rjudson
 * @version $Revision: 1.0 $
 */
public class LibSets {
    private LibSets() {
    }

    /**
     * Method create.
     * 
     * @param items
     *            C[]
     * @return Set<C>
     */
    public static <C> Set<C> create(C... items) {
        HashSet<C> ret = new HashSet<C>();
        ret.addAll(asList(items));
        return ret;
    }

    /**
     * Method join.
     * 
     * @param sets
     *            Set<C>[]
     * @return Set<C>
     */
    public static <C> Set<C> join(Set<C>... sets) {
        HashSet<C> ret = new HashSet<C>();
        for (Set<C> c : sets)
            ret.addAll(c);
        return ret;
    }

    /**
     * Method join.
     * 
     * @param lists
     *            Object[][]
     * @return Set<C>
     */
    @SuppressWarnings("unchecked")
    public static <C> Set<C> join(Object[]... lists) {
        HashSet<C> ret = new HashSet<C>();
        for (Object[] c : lists)
            ret.addAll((Collection<? extends C>) asList(c));
        return ret;
    }

    /**
     * Does the given set contain all of the items? If there are zero items it will be true.
     * 
     * @param set
     * @param items
     * @return boolean
     */
    public static <T> boolean all(Set<? super T> set, T... items) {
        if (items.length > 0)
            for (T i : items)
                if (!set.contains(i))
                    return false;
        return true;
    }

    /**
     * Does the given set contain any of the items? If there are no items, it will return false.
     * 
     * @param set
     * @param items
     * @return boolean
     */
    public static <T> boolean any(Set<? super T> set, T... items) {
        if (items.length > 0)
            for (T i : items)
                if (set.contains(i))
                    return true;
        return false;
    }

    /**
     * Packs the given EnumSet into an integer by assigning each potential value of the set to a bit in a bit mask.
     * 
     * @param eset
     * @return int
     */
    public static int packEnumSet(EnumSet<?> eset) {
        if (eset.isEmpty()) {
            // We don't need to know anything in this case.
            return 0;
        } else {
            assert eset.iterator().next().getClass().getEnumConstants().length <= 32;
            int result = 0;
            for (Enum<?> e : eset)
                result = result | (1 << e.ordinal());
            return result;

        }
    }

    /**
     * Unpack an integer into an EnumSet, given the class of the enums.
     * 
     * @param eclass
     *            Class<E>
     * @param value
     *            int
     * @return EnumSet<E>
     */
    public static <E extends Enum<E>> EnumSet<E> unpackEnumSet(Class<E> eclass, int value) {
        EnumSet<E> ret = EnumSet.noneOf(eclass);
        for (E e : eclass.getEnumConstants())
            if ((value & (1 << e.ordinal())) != 0)
                ret.add(e);
        return ret;
    }

}
