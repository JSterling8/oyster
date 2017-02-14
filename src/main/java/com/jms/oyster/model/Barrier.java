package com.jms.oyster.model;

import java.util.Set;

public class Barrier {
    public enum Type {
        BUS, TUBE
    }

    public enum Direction {
        IN, OUT
    }

    private final Set<Integer> zones;
    private final String name;
    private final Type type;
    private final Direction direction;

    public Barrier(Set<Integer> zones, String name, Type type, Direction direction) {
        this.zones = zones;
        this.name = name;
        this.type = type;
        this.direction = direction;
    }

    public Set<Integer> getZones() {
        return zones;
    }

    public String getName() {
        return name;
    }

    public Type getType() {
        return type;
    }

    public Direction getDirection() {
        return direction;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Barrier barrier = (Barrier) o;

        if (!zones.equals(barrier.zones)) return false;
        if (!name.equals(barrier.name)) return false;
        if (type != barrier.type) return false;
        return direction == barrier.direction;

    }

    @Override
    public int hashCode() {
        int result = zones.hashCode();
        result = 31 * result + name.hashCode();
        result = 31 * result + type.hashCode();
        result = 31 * result + direction.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Barrier{" +
                "zones=" + zones +
                ", name='" + name + '\'' +
                ", type=" + type +
                ", direction=" + direction +
                '}';
    }
}
