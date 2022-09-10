package com.logging;

import org.apache.logging.log4j.MarkerManager;
import org.slf4j.IMarkerFactory;
import org.slf4j.Marker;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class Log4jMarker implements Marker {

    public static final long serialVersionUID = 1570372L;

    private final IMarkerFactory factory;
    private final org.apache.logging.log4j.Marker marker;

    public Log4jMarker(final IMarkerFactory markerFactory,
                       final org.apache.logging.log4j.Marker marker
    ) {
        this.factory = markerFactory;
        this.marker = marker;
    }

    public void add(final Marker marker) {
        if (marker == null) {
            throw new IllegalArgumentException();
        }

        Marker m = this.factory.getMarker(marker.getName());
        this.marker.addParents(((Log4jMarker) m).getLog4jMarker());

    }

    public boolean contains(final Marker marker) {
        if (marker == null) {
            throw new IllegalArgumentException();
        }

        return this.marker.isInstanceOf(marker.getName());

    }

    public boolean contains(final String s) {
        return s != null && this.marker.isInstanceOf(s);
    }

    public boolean equals(final Object object) {
        if (this == object) {
            return true;
        }
        if (object == null) {
            return false;
        }
        if (!(object instanceof Log4jMarker)) {
            return false;
        }

        final Log4jMarker other = (Log4jMarker) object;
        return Objects.equals(this.marker, other.marker);

    }

    public org.apache.logging.log4j.Marker getLog4jMarker() {
        return this.marker;
    }

    public String getName() {
        return this.marker.getName();
    }

    @Deprecated
    public boolean hasChildren() {
        return this.marker.hasParents();
    }

    public int hashCode() {
        return 31 + Objects.hashCode(this.marker);
    }

    public boolean hasReferences() {
        return this.marker.hasParents();
    }

    public Iterator<Marker> iterator() {
        org.apache.logging.log4j.Marker[] log4jParents = this.marker.getParents();
        List<Marker> parents = new ArrayList<>(log4jParents.length);

        for (org.apache.logging.log4j.Marker m : log4jParents) {
            parents.add(this.factory.getMarker(m.getName()));
        }

        return parents.iterator();
    }

    public boolean remove(final Marker marker) {
        return marker != null && this.marker.remove(MarkerManager.getMarker(marker.getName()));
    }
}
