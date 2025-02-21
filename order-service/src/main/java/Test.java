import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Test {
    public static void main(String[] args) {
        String s="ABCBcKAD";

        System.out.println(s.chars()
                .mapToObj(character-> Character.toLowerCase((char) character))
                .collect(Collectors.groupingBy(Function.identity(), LinkedHashMap::new, Collectors.counting()))
                .entrySet()
                .stream()
                .filter(characterLongEntry -> characterLongEntry.getValue()==1)
                .map(Map.Entry::getKey)
                .findFirst()
                .get());


    }
}
