package util;

import org.apache.rocketmq.common.message.Message;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @Package: util
 * @ClassName: ListSplitter
 * @Author: AZ
 * @CreateTime: 2021/8/16 23:16
 * @Description:
 */
public class ListSplitter implements Iterator<List<Message>> {

    // 限制大小
    private final int SIZE_LIMIT = 1000 * 1000;
    // 信息容器存储
    private final List<Message> messages;
    // 当前索引下标
    private int currIndex;

    public ListSplitter(List<Message> messages) {
        this.messages = messages;
    }

    @Override
    public boolean hasNext() {
        return currIndex < messages.size();
    }

    @Override
    public List<Message> next() {
        int nextIndex = currIndex;
        int totalSize = 0;

        // for循环截取消息体长度
        for(; nextIndex < messages.size(); nextIndex++){
            Message message = messages.get(nextIndex);
            // 拆分消息体长度计算
            int tmpSize = message.getTopic().length() + message.getBody().length;
            Map<String, String> properties = message.getProperties();
            for(Map.Entry<String, String> entry : properties.entrySet()){
                tmpSize += entry.getKey().length() + entry.getValue().length();
            }
            tmpSize = tmpSize + 20; // for log overhead
            // 一个message就超过最大长度限制
            if(tmpSize > SIZE_LIMIT){
                //it is unexpected that single message exceeds the sizeLimit
                //here just let it go, otherwise it will block the splitting process
                if(nextIndex - currIndex == 0){
                    //if the next sublist has no element, add this one and then break, otherwise just break
                    nextIndex++;
                }
                break;
            }

            // 每次偏移长度加上已经拆分的长度
            if(tmpSize + totalSize > SIZE_LIMIT){
                break;
            }else{
                totalSize += tmpSize;
            }
        }
        List<Message> subList = messages.subList(currIndex, nextIndex);
        currIndex = nextIndex;
        return subList;
    }
}
