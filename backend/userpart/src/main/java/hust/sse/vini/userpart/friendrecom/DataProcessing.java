package hust.sse.vini.userpart.friendrecom;

import hust.sse.vini.userpart.user.UserInterestOptions;
import net.sf.javaml.core.SparseInstance;

import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class DataProcessing {
    public static double[] interestsToOneHot(Iterable<String> interests){
        double[] onehot=new double[UserInterestOptions.getInterestLength()+1];
        for(int i=0;i<onehot.length;i++){
            onehot[i]=0;
        }
        for(String interest:interests){
            onehot[UserInterestOptions.getInterestNumber(interest)]=1;
        }
        return onehot;
    }

    public static double[] attributeMinus(double[] attr1,double[] attr2){
        if(attr1.length!=attr2.length)return null;
        double[] res=new double[attr1.length];
        IntStream.range(0,attr1.length).forEach(
                (x)->{
                    res[x]=Math.abs(attr1[x]-attr2[x]);
                    return;
                }
        );
        return res;
    }
}
