package wily.betterfurnaces.util;

import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import net.minecraft.core.Direction;

public class DirectionUtil {
    public static Quaternion getRotation(Direction direction) {
        switch (direction) {
            case DOWN -> {return Vector3f.XP.rotationDegrees(180.0F);}
            case UP -> {return Quaternion.ONE;
            }
            case NORTH -> {
                return Vector3f.XP.rotationDegrees(-90.0F);

            }
            case SOUTH -> {
                return Vector3f.XP.rotationDegrees(90.0F);
            }
            case WEST -> {
                return Vector3f.ZP.rotationDegrees(90.0F);

            }
            case EAST -> {
                return Vector3f.ZP.rotationDegrees(-90.0F);

            }
            default -> throw new IncompatibleClassChangeError();
        }
    }
    public static Quaternion getHorizontalRotation(Direction direction) {
        switch (direction) {
            case NORTH -> {
                return Quaternion.ONE;

            }
            case SOUTH -> {
                return Vector3f.YP.rotationDegrees(180.0F);
            }
            case WEST -> {
                return Vector3f.YP.rotationDegrees(90.0F);

            }
            case EAST -> {
                return Vector3f.YP.rotationDegrees(-90.0F);

            }
            default -> throw new IncompatibleClassChangeError();
        }
    }
}
