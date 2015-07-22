package himmel.graphics.renderers;

/**
 * Created by Igor on 22-Jul-15.
 */
public class ShaderComponent {
    private final int location;
    private final TYPE type;
    private final int amount;

    public ShaderComponent(final int location, final TYPE type, final int amount) {
        this.location = location;
        this.type = type;
        this.amount = amount;
    }

    public final int getLocation() {
        return location;
    }

    public final TYPE getType() {
        return type;
    }

    public final int getAmount() {
        return amount;
    }


    public enum TYPE {
        FLOAT(4);

        private final int bytes;

        TYPE(final int bytes) {
            this.bytes = bytes;
        }

        public final int getBytes() {
            return bytes;
        }
    }
}
