package pers.xyy.api_replacement_tool.utils;

import org.springframework.stereotype.Service;

import java.util.LinkedList;

@Service
public class BoundedStack<E> extends LinkedList<E> {

    private int maxSize;

    public BoundedStack() {
        this(6);
    }

    public BoundedStack(int maxSize) {
        this.maxSize = maxSize;
    }

    public void push(E e) {
        while (this.size() >= maxSize)
            removeFirst();
        addLast(e);
    }

    public E pop() {
        return removeLast();
    }

    public E peek() {
        return getLast();
    }

}
