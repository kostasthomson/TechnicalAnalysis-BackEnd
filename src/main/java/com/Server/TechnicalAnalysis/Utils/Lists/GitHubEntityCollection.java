package com.Server.TechnicalAnalysis.Utils.Lists;

import com.Server.TechnicalAnalysis.Models.GitHubCommit;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Consumer;

public abstract class GitHubEntityCollection<T> implements List<T> {
    protected List<T> list = new ArrayList<>();
    protected Map<String, Integer> map = new HashMap<>();

    public abstract boolean add(T object);
    public abstract boolean remove(Object o);

    public T get(String key) {
        Integer index = map.get(key);
        return (index != null) ? list.get(index) : null;
    }
    @Override
    public T get(int index) {
        return list.get(index);
    }
    @Override
    public @NotNull ListIterator<T> listIterator() {
        return list.listIterator();
    }

    @Override
    public @NotNull ListIterator<T> listIterator(int index) {
        return list.listIterator(index);
    }

    @Override
    public @NotNull List<T> subList(int fromIndex, int toIndex) {
        return list.subList(fromIndex, toIndex);
    }

    @Override
    public int size() {
        return list.size();
    }

    @Override
    public boolean isEmpty() {
        return list.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return list.contains(o);
    }

    @Override
    public @NotNull Iterator<T> iterator() {
        return list.iterator();
    }

    @Override
    public void forEach(Consumer<? super T> action) {
        list.forEach(action);
    }

    @Override
    public Spliterator<T> spliterator() {
        return list.spliterator();
    }

    @Override
    public void clear() {
        map.clear();
        list.clear();
    }

    @NotNull
    @Override
    public Object[] toArray() {
        return list.toArray();
    }

    @NotNull
    @Override
    public <T> T[] toArray(@NotNull T[] a) {
        return list.toArray(a);
    }

    @Override
    public boolean containsAll(@NotNull Collection<?> c) {
        return new HashSet<>(list).containsAll(c);
    }

    @Override
    public boolean addAll(@NotNull Collection<? extends T> c) {
        return list.addAll(c);
    }

    @Override
    public boolean addAll(int index, @NotNull Collection<? extends T> c) {
        return list.addAll(index, c);
    }

    @Override
    public boolean removeAll(@NotNull Collection<?> c) {
        return list.removeAll(c);
    }

    @Override
    public boolean retainAll(@NotNull Collection<?> c) {
        return list.retainAll(c);
    }

    @Override
    public T set(int index, T element) {
        return list.set(index, element);
    }

    @Override
    public void add(int index, T element) {
        list.add(index, element);
    }

    @Override
    public T remove(int index) {
        return list.remove(index);
    }

    @Override
    public int indexOf(Object o) {
        return list.indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        return list.lastIndexOf(o);
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        list.forEach(element -> stringBuilder.append(element).append("\n"));
        return stringBuilder.toString();
    }
}
