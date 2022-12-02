package work.yspan.mapper;

import work.yspan.domain.UserDO;
import work.yspan.domain.VideoOrderDO;


import java.util.List;

public interface VideoOrderMapper {

    //一对多查询
    List<UserDO> queryUserOrder();

    //懒加载
    List<VideoOrderDO> queryOrderLazyUser();
}
