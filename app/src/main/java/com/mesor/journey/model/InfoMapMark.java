package com.mesor.journey.model;

/**
 * Created by Limeng on 2016/8/27.
 */
public class InfoMapMark {
    private String _name;
    private String _location;
    private String coordtype = "autonavi";
    private String _address;

    public String get_name() {
        return _name;
    }

    public void set_name(String _name) {
        this._name = _name;
    }

    public String get_location() {
        return _location;
    }

    public void set_location(String _location) {
        this._location = _location;
    }

    public String getCoordtype() {
        return coordtype;
    }

    public void setCoordtype(String coordtype) {
        this.coordtype = coordtype;
    }

    public String get_address() {
        return _address;
    }

    public void set_address(String _address) {
        this._address = _address;
    }
}
