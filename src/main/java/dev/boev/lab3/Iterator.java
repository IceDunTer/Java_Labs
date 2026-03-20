package dev.boev.lab3;

public interface Iterator
{
    public boolean hasNext();
    public Object next();
    public Object preview();
    public boolean hasPreview();
    public int getCurrentIndex();
    }
