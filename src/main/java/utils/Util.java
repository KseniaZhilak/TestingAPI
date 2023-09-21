package utils;

import java.util.List;
import static org.testng.Assert.assertEquals;

public class Util {
    // Метод проверяет, является ли коллекция возрастающей последовательностью
    public static boolean isSortedList(List<Integer> list){
        for (int i = 0; i < list.size() - 1; i++) {
           if(list.get(i) > list.get(i + 1)){
               return false;
           }
        }
        return true;
    }
}
