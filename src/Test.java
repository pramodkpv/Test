import java.lang.reflect.Array;
import java.math.BigInteger;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class Test {
    private static final String pan ="4567891234567890";
    public static void main(String args[]){
        Test t = new Test();
        t.getMetaDataJava8();
        //t.getMetaDataJava7();

    }

    private static Predicate<Profile> isBinRangesMatches(final BigInteger subBin){
        System.out.println("isBinRangesMatches subBin="+subBin);
        String strStartBinRange = subBin.toString();
        String strEndBinRange = subBin.toString();
        for(int i = 0 ; i< 16- subBin.toString().length(); i++){
            strStartBinRange = strStartBinRange + "0";
            strEndBinRange = strEndBinRange + "9";
        }
        System.out.println(" strStartBinRange = "+strStartBinRange + " strEndBinRange ="+ strEndBinRange);
        final BigInteger startBinRange = new BigInteger(strStartBinRange);
        final BigInteger endBinRange = new BigInteger(strEndBinRange);
        return p ->p.getStartRange().equals(startBinRange)
                && p.getEndRange().equals(endBinRange);
    }

    private static Predicate<Profile> isBinAndSubBinMatches(final BigInteger subBin) {
        System.out.println(" isBinAndSubBinMatches  subBin="+subBin);
        Predicate<Profile> value =  p -> p.getBin().equals(subBin)
                && p.getStartRange() != null
                && p.getEndRange() != null
                && p.getMetaData() != null;

        return value;
    }


    private Profile getMetaDataBySubBinPredicate(final List<Profile> profileList,
                                                 final BigInteger subBin)
                                                // final Predicate<Profile> subBinP,
                                                // final Predicate<Profile> subBinRangeP)
    {
        System.out.println("getMetaDataBySubBinPredicate subBin="+subBin);// + "subBinP ="+ subBinP + " subBinRangeP "+ subBinRangeP);
        profileList.forEach(System.out::println);
        return profileList.stream()
                .filter( p -> isBinAndSubBinMatches(subBin).test(p))
                .filter(p -> isBinRangesMatches(subBin).test(p))
                //.filter(subBinRangeP)
                .findFirst().orElse(null);

    }
    private void getMetaDataJava8(){
        System.out.println("getMetaDataJava8");
        List<Profile> profileList = getProfileList();
        System.out.println("profileList size="+profileList.size());
        BigInteger subBin = new BigInteger(pan);
        for(int i =0; i < pan.length() - 6; i++) {
            subBin = subBin.divide(BigInteger.TEN);
            Profile profile = getMetaDataBySubBinPredicate(profileList,subBin);//, isBinAndSubBinMatches(subBin), isBinRangesMatches(subBin));
            System.out.println("profile ="+profile);
            if(profile != null){
                System.out.println("profile Predicate=>>"+profile.toString());
                break;
            }
        }
        /*
        for(int i =0; i < pan.length() - 6; i++) {
            subBin = subBin.divide(BigInteger.TEN);
            Profile profile = getMetaDataBySubBin(profileList,subBin);
            if(profile != null){
                System.out.println("profile  "+profile.toString());
                break;
            }
        }
        */

        //subBin = new BigInteger(pan);
   }



    private Profile getMetaDataBySubBin(final List<Profile> profileList, final BigInteger subBin){
        System.out.println("getMetaDataBySubBin subBin="+subBin);
        return profileList.stream()
                   .filter(p->p.getBin().equals(subBin))
                   .filter(p -> p.getStartRange() != null && p.getEndRange() != null)
                   .findFirst().orElse(null);

    }

    public Collector<BigInteger, ?, BigInteger> divide() {
        return Collectors.reducing(BigInteger.ZERO, BigInteger::divide);
    }

    private Profile getMetaDataJava7(){
        System.out.println("getMetaDataJava7");
        BigInteger subBin = new BigInteger(pan);
        List<Profile> profileList = getProfileList();
        System.out.println("profileList size="+profileList.size());
        String metaData = null;
        for(int i =0; i < pan.length() - 6; i++){
            subBin = subBin.divide(BigInteger.TEN);
            for(Profile profile: profileList){
                if(subBin.equals(profile.getBin())){
                    metaData = profile.getMetaData();
                    System.out.println("getMetaDataJava7 subBin="+subBin + " bin=" + profile.getBin() + " metaData="+ metaData);
                    return profile;
                }
            }
        }
        return null;
    }
    private List<Profile> getProfileList(){
        List<Profile> profileList = new ArrayList<>();
/*
        Profile p = new Profile();

       p.setBin(new BigInteger("45678"));
        p.setStartRange(new BigInteger("4567800000000000"));
        p.setEndRange(new BigInteger("4567899999999999"));
        p.setMetaData("metadata_45678");
        profileList.add(p);
*/

       profileList.add(new Profile(
                new BigInteger("4567891234"),
                new BigInteger("4567891234000000"),
                new BigInteger("4567891234999999"),
                "metadata_4567891234"));

/*
      profileList.add(new Profile(
                new BigInteger("4567891"),
                new BigInteger("4567891000000000"),
                new BigInteger("4567891999999999"),
                "metadata_4567891"));

        profileList.add(new Profile(
                new BigInteger("45678912"),
                new BigInteger("4567891200000000"),
                new BigInteger("4567891299999999"),
                "metadata_45678912"));

        profileList.add(new Profile(
                new BigInteger("456789123"),
                new BigInteger("4567891230000000"),
                new BigInteger("4567891239999999"),
                "metadata_456789123"));

        profileList.add(new Profile(
                new BigInteger("4567891234"),
                new BigInteger("4567891234000000"),
                new BigInteger("4567891223999999"),
                "metadata_4567891234"));
        */
        profileList.add(new Profile(
                new BigInteger("45678912345"),
                new BigInteger("4567891234500000"),
                new BigInteger("4567891234599999"),
                "metadata_45678912345"));


        return profileList;
    }
    class Profile{
        BigInteger bin;
        BigInteger startRange;
        BigInteger endRange;
        String metaData;

        Profile(){

        }
        Profile(BigInteger bin, BigInteger startRange, BigInteger endRange, String metaData){
            this.bin = bin;
            this.endRange=endRange;
            this.startRange=startRange;
            this.metaData=metaData;
        }

        public BigInteger getBin() {
            return bin;
        }

        public void setBin(BigInteger bin) {
            this.bin = bin;
        }

        public BigInteger getStartRange() {
            return startRange;
        }

        public void setStartRange(BigInteger startRange) {
            this.startRange = startRange;
        }

        public BigInteger getEndRange() {
            return endRange;
        }

        public void setEndRange(BigInteger endRange) {
            this.endRange = endRange;
        }

        public String getMetaData() {
            return metaData;
        }

        public void setMetaData(String metaData) {
            this.metaData = metaData;
        }

        @Override
        public String toString() {
            return "Profile{" +
                    "bin=" + bin +
                    ", startRange=" + startRange +
                    ", endRange=" + endRange +
                    ", metaData='" + metaData + '\'' +
                    '}';
        }
    }
}
