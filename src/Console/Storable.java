package Console;

/**
 * Created by Isabel on 27.11.2016.
 */
public interface Storable {
    Storable deep_copy();
    String getName();
    void setName(String name);
}
