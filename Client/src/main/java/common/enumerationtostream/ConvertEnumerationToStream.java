package common.enumerationtostream;

import java.util.Enumeration;
import java.util.Spliterator;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class ConvertEnumerationToStream {
    public static <T> Stream<T> convert(Enumeration<T> enumeration) {
        EnumerationSpliterator<T> spliterator
                = new EnumerationSpliterator<>(Long.MAX_VALUE, Spliterator.ORDERED, enumeration);
        return StreamSupport.stream(spliterator, false);
    }
}
