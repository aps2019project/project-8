package model;

public class Target {
    private TargetUnit targetUnit;
    private TargetUnitType targetUnitType;

    public Target(TargetUnit targetUnit, TargetUnitType targetUnitType) {
        this.targetUnit = targetUnit;
        this.targetUnitType = targetUnitType;
    }

    public enum TargetUnit {
        FRIENDLY_UNIT,
        FRIENDLY_MINION,
        FRIENDLY_HERO,
    }
    public enum TargetUnitType {
        ALL,
        MELEE,
        RANGED,
        HYBRID,
        MELEE_RANGED,
        MELEE_HYBRID,
        RANGED_HYBRID
    }
}
