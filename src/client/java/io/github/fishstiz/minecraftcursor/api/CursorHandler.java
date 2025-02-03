package io.github.fishstiz.minecraftcursor.api;

import com.google.common.reflect.TypeToken;
import io.github.fishstiz.minecraftcursor.cursor.CursorType;
import net.minecraft.client.gui.Element;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

/**
 * This interface defines a handler for associating a {@link CursorType} with a specific type of {@link Element}.
 * Implementing this interface allows you to define how the cursor should change when hovering over a given element.
 *
 * <p>The implementation should provide a cursor type based on the provided element and mouse coordinates.</p>
 *
 * @param <T> the type of the {@link Element} the cursor handler is associated with.
 *           <br><br>
 *            If the target {@link Element} is inaccessible, you can pass {@link Element}
 *            as a generic type and override the {@link #getTargetElement()} method to use the fully qualified
 *            class name (FQCN) of your target element.
 */
public interface CursorHandler<T extends Element> {
    /**
     * Gets the target element associated with this cursor handler.
     * The target element is either determined by the element class or the fully qualified class name.
     *
     * @return a {@link TargetElement} containing either the element class or its fully qualified class name
     */
    @SuppressWarnings("unchecked")
    default @NotNull TargetElement<T> getTargetElement() {
        TypeToken<T> typeToken = new TypeToken<>(getClass()) {
        };
        return TargetElement.fromClass((Class<T>) typeToken.getRawType());
    }

    /**
     * Retrieves the cursor type to be applied when the mouse is over the target element.
     *
     * @param element the element the cursor is hovering over
     * @param mouseX  the X coordinate of the mouse
     * @param mouseY  the Y coordinate of the mouse
     * @return the {@link CursorType} to be applied
     */
    CursorType getCursorType(T element, double mouseX, double mouseY);

    /**
     * A record that represents the target element associated with a {@link CursorHandler}.
     * It stores either the {@link Class} of the target element or its fully qualified class name (FQCN).
     *
     * <p>The fully qualified class name can be used when the target element is inaccessible, allowing
     * for reflection-based access to the class.</p>
     *
     * @param <T> the type of the {@link Element}
     */
    record TargetElement<T extends Element>(
            Optional<Class<T>> elementClass,
            Optional<String> fullyQualifiedClassName
    ) {
        /**
         * Creates a {@link TargetElement} from the given element class.
         *
         * @param elementClass the class of the target element
         * @param <T>          the type of the {@link Element}
         * @return a {@link TargetElement} containing the element class
         */
        public static <T extends Element> TargetElement<T> fromClass(Class<T> elementClass) {
            return new TargetElement<>(Optional.of(elementClass), Optional.empty());
        }

        /**
         * Creates a {@link TargetElement} from the given fully qualified class name.
         *
         * @param fullyQualifiedClassName the fully qualified class name of the target element
         * @param <T>                     the type of the {@link Element}
         * @return a {@link TargetElement} containing the fully qualified class name for reflection
         */
        public static <T extends Element> TargetElement<T> fromClassName(String fullyQualifiedClassName) {
            return new TargetElement<>(Optional.empty(), Optional.of(fullyQualifiedClassName));
        }
    }
}
