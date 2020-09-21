package sse.hust.vini.user;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;

public class UserInterestOptions{
    private static String[] interestOption={
            "吃饭","睡觉","音乐","阅读","运动","电影","旅游","小说","电视剧","游戏",
            "社交","绘画","跳舞","种植","烹饪","动漫","摄影","追星","Westlife","微积分","澳哥哥"
    };
    private static HashMap<String,Integer> interestMap=new HashMap<>(){
        {
            for (int i = 0; i < interestOption.length; i++) {
                put(interestOption[i],i);
            }
        }
    };

    public static int getInterestLength(){
        return interestOption.length;
    }

    public static Collection<String> getAllInterests(){
        return Arrays.asList(interestOption);
    }

    public static Integer getInterestNumber(String interest){
        if(interestMap.containsKey(interest)){
            return interestMap.get(interest);
        }else{
            return interestOption.length;
        }
    }

    public static String getInterest(int interestNumber){
        return interestOption[interestNumber];
    }
}
