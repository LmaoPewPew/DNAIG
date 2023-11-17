package com.softpro.dnaig.preview;

public enum Mode {

    ERROR("01100101 01110010 01110010 01101111 01110010", ModeType.ERROR),
    MOVE_CAMERA_FREE("move camera (free)", ModeType.CAMERA),
    MOVE_CAMERA_X("move camera (x)", ModeType.CAMERA),
    MOVE_CAMERA_Y("move camera (y)", ModeType.CAMERA),
    MOVE_CAMERA_Z("move camera (z)", ModeType.CAMERA),
    MOVE_OBJECT_FREE("move object (free)", ModeType.OBJECT),
    MOVE_OBJECT_X("move object (x)", ModeType.OBJECT),
    MOVE_OBJECT_Y("move object (y)", ModeType.OBJECT),
    MOVE_OBJECT_Z("move object (z)", ModeType.OBJECT),
    ;

    private String name;
    private ModeType type;

    Mode(String name, ModeType type) {
        this.name = name;
        this.type = type;
    }

    @Override
    public String toString() {
        return name;
    }

    public ModeType getType() {
        return type;
    }

    protected enum ModeType {
        CAMERA,
        OBJECT,
        ERROR
    }
}