package work.yspan.mapper;


import org.apache.ibatis.annotations.Param;
import work.yspan.domain.Video;
import work.yspan.domain.VideoOrderDO;

import java.util.List;
import java.util.Map;

public interface VideoMapper {
    Video selectByIdWithReslutMap(@Param("video_id") int videoId);

    Video selectById(@Param("video_id") int videoId);


    List<Video> selectByPointAndTitleLike(@Param("point") double point,@Param("title") String title);

    int add(Video video);

    int addBatch(List<Video> list);

    int update(Video video);

    int updateSelective(Video video);

    int deleteByPointAndPrice(Map<String,Object> map);

    List<VideoOrderDO> queryVideoOrderList();



}
