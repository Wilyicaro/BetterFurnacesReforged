package wily.betterfurnaces.util;

import com.mojang.math.Axis;
import net.minecraft.core.Direction;
import org.joml.Quaternionf;

public class DirectionUtil {
    public static Quaternionf getRotation(Direction direction) {
        switch (direction) {
            case DOWN -> {return Axis.XP.rotationDegrees(180.0F);}
            case UP -> {return new Quaternionf();
            }
            case NORTH -> {
                return Axis.XP.rotationDegrees(-90.0F);

            }
            case SOUTH -> {
                return Axis.XP.rotationDegrees(90.0F);
            }
            case WEST -> {
                return Axis.ZP.rotationDegrees(90.0F);

            }
            case EAST -> {
                return Axis.ZP.rotationDegrees(-90.0F);

            }
            default -> throw new IncompatibleClassChangeError();
        }
    }
    public static Quaternionf getHorizontalRotation(Direction direction) {
        switch (direction) {
            case NORTH -> {
                return new Quaternionf();

            }
            case SOUTH -> {
                return Axis.YP.rotationDegrees(180.0F);
            }
            case WEST -> {
                return Axis.YP.rotationDegrees(90.0F);

            }
            case EAST -> {
                return Axis.YP.rotationDegrees(-90.0F);

            }
            default -> throw new IncompatibleClassChangeError();
        }
    }
}
