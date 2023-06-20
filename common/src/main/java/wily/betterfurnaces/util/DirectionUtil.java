package wily.betterfurnaces.util;

import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import net.minecraft.core.Direction;


public class DirectionUtil {
    public static Quaternion getRotation(Direction direction) {
        if (direction == Direction.DOWN) return Vector3f.XP.rotationDegrees(180.0F);
        else if (direction == Direction.NORTH) return Vector3f.XP.rotationDegrees(-90.0F);
        else if  (direction == Direction.SOUTH) return Vector3f.XP.rotationDegrees(90.0F);
        else if  (direction == Direction.WEST) return Vector3f.ZP.rotationDegrees(90.0F);
        else if  (direction == Direction.EAST) return Vector3f.ZP.rotationDegrees(-90.0F);
        else return Quaternion.ONE;
    }
    public static Quaternion getHorizontalRotation(Direction direction) {
        if (direction == Direction.NORTH) return Quaternion.ONE;
        else if (direction == Direction.SOUTH) return Vector3f.YP.rotationDegrees(180.0F);
        else if (direction == Direction.WEST) return Vector3f.YP.rotationDegrees(90.0F);
        else if (direction == Direction.EAST) return Vector3f.YP.rotationDegrees(-90.0F);
        else throw new IncompatibleClassChangeError();
    }
}
