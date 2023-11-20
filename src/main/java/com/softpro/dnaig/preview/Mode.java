package com.softpro.dnaig.preview;

public enum Mode {

    ERROR("01100101 01110010 01110010 01101111 01110010", TargetType.ERROR, ActionType.ERROR),
    MOVE_CAMERA_FREE("move camera (free)", TargetType.CAMERA, ActionType.MOVE),
    MOVE_CAMERA_X("move camera (x)", TargetType.CAMERA, ActionType.MOVE),
    MOVE_CAMERA_Y("move camera (y)", TargetType.CAMERA, ActionType.MOVE),
    MOVE_CAMERA_Z("move camera (z)", TargetType.CAMERA, ActionType.MOVE),
    MOVE_OBJECT_FREE("move object (free)", TargetType.OBJECT, ActionType.MOVE),
    MOVE_OBJECT_X("move object (x)", TargetType.OBJECT, ActionType.MOVE),
    MOVE_OBJECT_Y("move object (y)", TargetType.OBJECT, ActionType.MOVE),
    MOVE_OBJECT_Z("move object (z)", TargetType.OBJECT, ActionType.MOVE),
    ROTATE_CAMERA_FREE("rotate camera (free)", TargetType.CAMERA, ActionType.ROTATE),
    ROTATE_CAMERA_X("rotate camera (x)", TargetType.CAMERA, ActionType.ROTATE),
    ROTATE_CAMERA_Y("rotate camera (y)", TargetType.CAMERA, ActionType.ROTATE),
    ROTATE_CAMERA_Z("rotate camera (z)", TargetType.CAMERA, ActionType.ROTATE),
    ROTATE_OBJECT_FREE("rotate object (free)", TargetType.OBJECT, ActionType.ROTATE),
    ROTATE_OBJECT_X("rotate object (x)", TargetType.OBJECT, ActionType.ROTATE),
    ROTATE_OBJECT_Y("rotate object (y)", TargetType.OBJECT, ActionType.ROTATE),
    ROTATE_OBJECT_Z("rotate object (z)", TargetType.OBJECT, ActionType.ROTATE),
    ;

    private final String name;
    private final TargetType targetType;
    private final ActionType actionType;

    Mode(String name, TargetType targetType, ActionType actionType) {
        this.name = name;
        this.targetType = targetType;
        this.actionType = actionType;
    }

    @Override
    public String toString() {
        return name;
    }

    public TargetType getTargetType() {
        return targetType;
    }

    protected enum TargetType {
        CAMERA,
        OBJECT,
        ERROR
    }

    protected enum ActionType {
        MOVE,
        ROTATE,
        ERROR
    }
}