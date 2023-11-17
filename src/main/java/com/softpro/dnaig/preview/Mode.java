package com.softpro.dnaig.preview;

public enum Mode {

    ERROR("01100101 01110010 01110010 01101111 01110010"),
    MOVE_CAMERA_FREE("move camera (free)"),
    MOVE_CAMERA_X("move camera (x)"),
    MOVE_CAMERA_Y("move camera (y)"),
    MOVE_CAMERA_Z("move camera (z)"),
    MOVE_OBJECT_FREE("move object (free)"),
    MOVE_OBJECT_X("move object (x)"),
    MOVE_OBJECT_Y("move object (y)"),
    MOVE_OBJECT_Z("move object (z)"),
    ;

    private String name;

    Mode(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}