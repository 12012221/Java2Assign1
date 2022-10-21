import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Map.Entry.comparingByValue;
import static java.util.stream.Collectors.toMap;

public class MovieAnalyzer {
    ArrayList<String[]> arrayList = new ArrayList<>();
    public MovieAnalyzer(String dataset_path) throws IOException {
        FileInputStream inputStream = new FileInputStream(dataset_path);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
        String str = bufferedReader.readLine();
        String[] sp;
        while((str = bufferedReader.readLine()) != null) {
            sp = str.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
            for (int i = 0; i < sp.length; i++) {
                if (sp[i].startsWith("\"") && sp[i].endsWith("\""))
                    sp[i] = sp[i].substring(1, sp[i].length() - 1);
            }
            arrayList.add(sp);
        }
        inputStream.close();
        bufferedReader.close();
    }

    public Map<Integer, Integer> getMovieCountByYear(){
        Map<Integer,Integer> userTreeMap = new TreeMap<Integer,Integer>(new Comparator<Integer>() {
            public int compare(Integer key1, Integer key2) {
                return key2.compareTo(key1);
            }
        });
        Map<Integer, Integer> map = arrayList.stream()
                .sorted((v1, v2) -> v2[2].compareTo(v1[2]))
                .collect(Collectors.groupingBy(l -> Integer.parseInt(l[2]), Collectors.summingInt(a -> 1)));
        for (Integer key: map.keySet()) {
            userTreeMap.put(key, map.get(key));
        }
        return userTreeMap;
    }

    public Map<String, Integer> getMovieCountByGenre(){
        Map<String,Integer> map = arrayList.stream().collect(Collectors.groupingBy(l -> l[5], Collectors.summingInt(a -> 1)))
                .entrySet()
                .stream()
                .sorted(Collections.reverseOrder(comparingByValue()))
                .collect(
                        toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2,
                                LinkedHashMap::new));
        Map<String,Integer> map2 = new HashMap<>();
        for (String key: map.keySet()) {
            String[] sps = key.split(", ");
            for (String s : sps){
                if (map2.containsKey(s))
                    map2.put(s, map2.get(s) + map.get(key));
                else map2.put(s, map.get(key));
            }
        }
        Map<String,Integer> map3 = map2.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .sorted(Collections.reverseOrder(comparingByValue()))
                .collect(
                        toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2,
                                LinkedHashMap::new));
        return map3;
    }

    public Map<List<String>, Integer> getCoStarCount() {
        HashMap<String, List<String>> hashMap = new HashMap<>();
        List<List<String>> pairs = new ArrayList<>();
        for (int i = 0; i < arrayList.size(); i++) {
            for (int j = 10; j < 14; j++) {
                for (int k = j + 1; k < 14; k++) {
                    if (!hashMap.containsKey(arrayList.get(i)[k])) {
                        if (!hashMap.containsKey(arrayList.get(i)[j])) {
                            ArrayList<String> cow = new ArrayList<>();
                            cow.add(arrayList.get(i)[k]);
                            hashMap.put(arrayList.get(i)[j], cow);
                        }
                        else if (!hashMap.get(arrayList.get(i)[j]).contains(arrayList.get(i)[k]))
                            hashMap.get(arrayList.get(i)[j]).add(arrayList.get(i)[k]);
                    }
                    else if (!hashMap.get(arrayList.get(i)[k]).contains(arrayList.get(i)[j])) {
                        if (!hashMap.containsKey(arrayList.get(i)[j])) {
                            ArrayList<String> cow = new ArrayList<>();
                            cow.add(arrayList.get(i)[k]);
                            hashMap.put(arrayList.get(i)[j], cow);
                        }
                        else if (!hashMap.get(arrayList.get(i)[j]).contains(arrayList.get(i)[k]))
                            hashMap.get(arrayList.get(i)[j]).add(arrayList.get(i)[k]);
                    }
                }
            }
        }
        for (String key : hashMap.keySet()) {
            for (int i = 0; i < hashMap.get(key).size(); i++) {
                List<String> ll = new ArrayList<>();
                int pan = key.compareTo(hashMap.get(key).get(i));
                if (pan < 0){
                    ll.add(key);
                    ll.add(hashMap.get(key).get(i));
                }
                else {
                    ll.add(hashMap.get(key).get(i));
                    ll.add(key);
                }
                pairs.add(ll);
            }
        }
        Map<List<String>, Integer> map = new HashMap<>();
        for (int i = 0; i < pairs.size(); i++) {
            int cnt = 0;
            for (int j = 0; j < arrayList.size(); j++) {
                String[] m = arrayList.get(j);
                if (!Objects.equals(pairs.get(i).get(0), pairs.get(i).get(1))) {
                    if (pairs.get(i).get(0).equals(m[10]) && pairs.get(i).get(1).equals(m[11]))
                        cnt++;
                    if (pairs.get(i).get(0).equals(m[10]) && pairs.get(i).get(1).equals(m[12]))
                        cnt++;
                    if (pairs.get(i).get(0).equals(m[10]) && pairs.get(i).get(1).equals(m[13]))
                        cnt++;
                    if (pairs.get(i).get(0).equals(m[11]) && pairs.get(i).get(1).equals(m[12]))
                        cnt++;
                    if (pairs.get(i).get(0).equals(m[11]) && pairs.get(i).get(1).equals(m[13]))
                        cnt++;
                    if (pairs.get(i).get(0).equals(m[12]) && pairs.get(i).get(1).equals(m[13]))
                        cnt++;
                    if (pairs.get(i).get(0).equals(m[11]) && pairs.get(i).get(1).equals(m[10]))
                        cnt++;
                    if (pairs.get(i).get(0).equals(m[12]) && pairs.get(i).get(1).equals(m[10]))
                        cnt++;
                    if (pairs.get(i).get(0).equals(m[13]) && pairs.get(i).get(1).equals(m[10]))
                        cnt++;
                    if (pairs.get(i).get(0).equals(m[12]) && pairs.get(i).get(1).equals(m[11]))
                        cnt++;
                    if (pairs.get(i).get(0).equals(m[13]) && pairs.get(i).get(1).equals(m[11]))
                        cnt++;
                    if (pairs.get(i).get(0).equals(m[13]) && pairs.get(i).get(1).equals(m[12]))
                        cnt++;
                }
                else if (Objects.equals(pairs.get(i).get(0), pairs.get(i).get(1))) {
                    if (pairs.get(i).get(0).equals(m[10]) && pairs.get(i).get(1).equals(m[11]))
                        cnt++;
                    if (pairs.get(i).get(0).equals(m[10]) && pairs.get(i).get(1).equals(m[12]))
                        cnt++;
                    if (pairs.get(i).get(0).equals(m[10]) && pairs.get(i).get(1).equals(m[13]))
                        cnt++;
                    if (pairs.get(i).get(0).equals(m[11]) && pairs.get(i).get(1).equals(m[12]))
                        cnt++;
                    if (pairs.get(i).get(0).equals(m[11]) && pairs.get(i).get(1).equals(m[13]))
                        cnt++;
                    if (pairs.get(i).get(0).equals(m[12]) && pairs.get(i).get(1).equals(m[13]))
                        cnt++;
                }
            }
            map.put(pairs.get(i), cnt);
        }
        return map.entrySet().stream()
                .sorted(Collections.reverseOrder(comparingByValue()))
                .collect(
                        toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2,
                                LinkedHashMap::new));
    }

    public List<String> getTopMovies(int top_k, String by){
        Map<Integer,ArrayList<String>> userTreeMap = new TreeMap<Integer,ArrayList<String>>(new Comparator<Integer>() {
            public int compare(Integer key1, Integer key2) {
                return key2.compareTo(key1);
            }
        });
        if (Objects.equals(by, "runtime")){
            List<String> list = arrayList.stream().sorted(Comparator.comparingInt(a -> -Integer.parseInt(a[4].replace(" min", ""))))
                    .map(a -> a[1]+"}"+a[4].replace(" min", ""))
                    .collect(Collectors.toList());
            for (int i = 0; i < list.size(); i++) {
                int nu = Integer.parseInt(list.get(i).split("}")[1]);
                if (!userTreeMap.containsKey(nu)) {
                    ArrayList<String> ll = new ArrayList<>();
                    ll.add(list.get(i).split("}")[0]);
                    userTreeMap.put(nu, ll);
                }
                else userTreeMap.get(nu).add(list.get(i).split("}")[0]);
            }
            List<String> res = new ArrayList<>();
            for (Integer key : userTreeMap.keySet()){
                Collections.sort(userTreeMap.get(key));
            }
            int cnt = 0;
            boolean br = false;
            for (Integer key : userTreeMap.keySet()){
                for (int i = 0; i < userTreeMap.get(key).size(); i++) {
                    cnt++;
                    if (cnt <= top_k)
                        res.add(userTreeMap.get(key).get(i));
                    else {
                        br = true;
                        break;
                    }
                }
                if (br) break;
            }
            return res;
        }
        else if(Objects.equals(by, "overview")){
            List<String> list = arrayList.stream().sorted(Comparator.comparingInt(a -> -a[7].length()))
                    .map(a -> a[1]+"}"+a[7].length())
                    .collect(Collectors.toList());
            for (int i = 0; i < list.size(); i++) {
                int nu = Integer.parseInt(list.get(i).split("}")[1]);
                if (!userTreeMap.containsKey(nu)) {
                    ArrayList<String> ll = new ArrayList<>();
                    ll.add(list.get(i).split("}")[0]);
                    userTreeMap.put(nu, ll);
                }
                else userTreeMap.get(nu).add(list.get(i).split("}")[0]);
            }
            List<String> res = new ArrayList<>();
            for (Integer key : userTreeMap.keySet()){
                Collections.sort(userTreeMap.get(key));
            }
            int cnt = 0;
            boolean br = false;
            for (Integer key : userTreeMap.keySet()){
                for (int i = 0; i < userTreeMap.get(key).size(); i++) {
                    cnt++;
                    if (cnt <= top_k)
                        res.add(userTreeMap.get(key).get(i));
                    else {
                        br = true;
                        break;
                    }
                }
                if (br) break;
            }
            return res;
        }
        else return new ArrayList<>();
    }

    public List<String> getTopStars(int top_k, String by){
        ArrayList<String[]> arrayList2 = new ArrayList<>();
        if (Objects.equals(by, "rating")) {
            Map<String, Double> rank = new HashMap<>();
            List<String> result = new ArrayList<>();
            List<String> stars = new ArrayList<>();
            for (int i = 0; i < arrayList.size(); i++) {
                for (int j = 10; j < 14; j++) {
                    if (!stars.contains(arrayList.get(i)[j]))
                        stars.add(arrayList.get(i)[j]);
                }
            }
            for (int i = 0; i < stars.size(); i++) {
                int finalI = i;
                int cnt = (int) arrayList.stream()
                        .filter(m -> stars.get(finalI).equals(m[10]) || stars.get(finalI).equals(m[11]) ||
                                stars.get(finalI).equals(m[12]) || stars.get(finalI).equals(m[13]))
                        .count();
                ArrayList<String> ratings = (ArrayList<String>) arrayList.stream()
                        .filter(m -> stars.get(finalI).equals(m[10]) || stars.get(finalI).equals(m[11]) ||
                                stars.get(finalI).equals(m[12]) || stars.get(finalI).equals(m[13]))
                        .map(m -> m[6]).collect(Collectors.toList());
                double tot = 0;
                for (int j = 0; j < ratings.size(); j++) {
                    tot += Float.parseFloat(ratings.get(j));
                }
                rank.put(stars.get(i), (tot/cnt));
            }
            Map<String, Double> uti = rank.entrySet().stream()
                    .sorted(Map.Entry.comparingByKey())
                    .sorted(Collections.reverseOrder(comparingByValue()))
                    .limit(top_k)
                    .collect(
                            toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2,
                                    LinkedHashMap::new));
            for (String key : uti.keySet()) {
                result.add(key);
            }
            return result;
        }
        else if(Objects.equals(by, "gross")){
            for (int i = 0; i < arrayList.size(); i++) {
                String[] aa = new String[arrayList.get(i).length];
                for (int j = 0; j < arrayList.get(i).length; j++) {
                    aa[j] = arrayList.get(i)[j];
                }
                arrayList2.add(aa);
            }
            Map<String, Double> rank = new HashMap<>();
            List<String> result = new ArrayList<>();
            List<String> stars = new ArrayList<>();
            arrayList2.removeIf(strings -> strings.length < 16);
            for (int i = 0; i < arrayList2.size(); i++) {
                for (int j = 10; j < 14; j++) {
                    if (!stars.contains(arrayList2.get(i)[j]))
                        stars.add(arrayList2.get(i)[j]);
                }
            }
            for (int i = 0; i < stars.size(); i++) {
                int finalI = i;
                int cnt = (int) arrayList2.stream()
                        .filter(m -> stars.get(finalI).equals(m[10]) || stars.get(finalI).equals(m[11]) ||
                                stars.get(finalI).equals(m[12]) || stars.get(finalI).equals(m[13]))
                        .count();
                ArrayList<String> ratings = (ArrayList<String>) arrayList2.stream()
                        .filter(m -> stars.get(finalI).equals(m[10]) || stars.get(finalI).equals(m[11]) ||
                                stars.get(finalI).equals(m[12]) || stars.get(finalI).equals(m[13]))
                        .map(m -> m[m.length - 1].replace(",", "")).collect(Collectors.toList());
                long tot = 0;
                for (int j = 0; j < ratings.size(); j++) {
                    tot += Double.parseDouble(ratings.get(j));
                }
                rank.put(stars.get(i), (double) (tot/cnt));
            }
            Map<String, Double> uti = rank.entrySet().stream()
                    .sorted(Map.Entry.comparingByKey())
                    .sorted(Collections.reverseOrder(comparingByValue()))
                    .limit(top_k)
                    .collect(
                            toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2,
                                    LinkedHashMap::new));
            for (String key : uti.keySet()) {
                result.add(key);
            }
            return result;
        }
        else return new ArrayList<>();
    }

    public List<String> searchMovies(String genre, float min_rating, int max_runtime){
        return arrayList.stream()
                .filter(m -> Integer.parseInt(m[4].replace(" min", "")) <= max_runtime && Double.parseDouble(m[6]) >= min_rating && m[5].contains(genre))
                .map(m -> m[1])
                .sorted()
                .collect(Collectors.toList());
    }
}