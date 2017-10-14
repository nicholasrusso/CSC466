package DocumentClasses;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Methods to operate on TextVector's HashMaps.
 * <p>
 * Created by cgels on 10/11/17.
 */
public class TextVectorUtils {


    public static <K, V extends Comparable<? super V>> Map<K, V> sortByValueAscending(Map<K, V> map) {
        // https://stackoverflow.com/questions/109383/sort-a-mapkey-value-by-values-java  -- CARTER PAGE
        return map.entrySet()
                  .stream()
                  .sorted(Map.Entry.comparingByValue())
                  .collect(Collectors.toMap(
                          Map.Entry::getKey,
                          Map.Entry::getValue,
                          (e1, e2) -> e1,
                          LinkedHashMap::new
                  ));
    }

    public static <K, V extends Comparable<? super V>> Map<K, V> sortByValueDescending(Map<K, V> map) {
        // https://stackoverflow.com/questions/109383/sort-a-mapkey-value-by-values-java  -- CARTER PAGE
        return map.entrySet()
                  .stream()
                  .sorted(Map.Entry.comparingByValue(Collections.reverseOrder()))
                  .collect(Collectors.toMap(
                          Map.Entry::getKey,
                          Map.Entry::getValue,
                          (e1, e2) -> e1,
                          LinkedHashMap::new
                  ));
    }

//    public static <K, V extends Comparable<? super V>> Map<K, V> sortByKeyAscending(Map<K, V> map) {
//        // https://stackoverflow.com/questions/109383/sort-a-mapkey-value-by-values-java  -- CARTER PAGE
//        TreeMap<K, V> sorted = new TreeMap<>(map);
//
//        return map.entrySet()
//                .stream()
//                .sorted(Map.Entry.comparingByKey())
//                .collect(Collectors.toMap(
//                        Map.Entry::getKey,
//                        Map.Entry::getValue,
//                        (e1, e2) -> e1,
//                        LinkedHashMap::new
//                ));
//    }
}
