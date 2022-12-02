import org.apache.ibatis.session.SqlSession;
import work.yspan.domain.UserDO;
import work.yspan.domain.Video;
import work.yspan.domain.VideoOrderDO;
import work.yspan.mapper.VideoMapper;
import work.yspan.mapper.VideoOrderMapper;
import work.yspan.utils.Mybatis_utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class VideoApplication {
    public static void main(String[] args) {
        SqlSession sqlSession = Mybatis_utils.getsqlSession();
        VideoMapper mapper = sqlSession.getMapper(VideoMapper.class);
        VideoOrderMapper mapper1 = sqlSession.getMapper(VideoOrderMapper.class);


        /**
         * 懒加载
         */
        List<VideoOrderDO> videoOrderDOS = mapper1.queryOrderLazyUser();


        for (VideoOrderDO videoOrderDO:videoOrderDOS){
            System.out.println(videoOrderDO.getCreateTime());
//        涉及到懒加载参数时加载
            System.out.println(videoOrderDO.getUser().getName());
        }

        /**
         * 一对多查询collection
         */

//        List<UserDO> userDOS = mapper1.queryUserOrder();
//        System.out.println(userDOS.toString());

        /**
         * 一对一查询 association
         */
//        List<VideoOrderDO> videoOrderDOS = mapper.queryVideoOrderList();
//        System.out.println(videoOrderDOS.toString());

        /**
         *  ReslutMap使用
         */
//        Video video = mapper.selectByIdWithReslutMap(9);
//        System.out.println(video.toString());

        /**
         * 通过point price删除
         */
//        double point=2.2;
//        int price=99;
//        Map<String,Object> map=new HashMap<>();
//        map.put("point",point);
//        map.put("price",price);
//        int rows = mapper.deleteByPointAndPrice(map);
//        sqlSession.commit();

        /**
         * 更新不为NULL的字段
         */
//        Video video=new Video();
//        video.setTitle("update6");
//        video.setVideoId(21);
//        int rows = mapper.updateSelective(video);
//        sqlSession.commit();
//        System.out.println(rows);

        /**
         * 根据id查询
         */
//        Video video = mapper.selectById(9);
        /**
         * 模糊查询
         */
//        List<Video> list=mapper.selectByPointAndTitleLike(8.7,"高级");
//        System.out.println(video.toString());
//        System.out.println(list.toString());

        /**
         * 批量插入
         */
//        Video video=new Video();
//        video.setTitle("批量插入");
//        video.setSummary("inset into xxx");
//        video.setCoverImg("www.yspan.work");
//        video.setPrice(99);
//        video.setPoint(9.9);
//        video.setViewNum("999");
//        video.setCreateTime(null);
//        video.setOnline(1);
//
//        Video video2=new Video();
//        video2.setTitle("批量插入");
//        video2.setSummary("inset into xxx");
//        video2.setCoverImg("www.yspan.work");
//        video2.setPrice(99);
//        video2.setPoint(9.9);
//        video2.setViewNum("999");
//        video2.setCreateTime(null);
//        video2.setOnline(1);
//
//        List<Video> list=new ArrayList<>();
//        list.add(video);
//        list.add(video2);
//
//        int rows= mapper.addBatch(list);
//        sqlSession.commit();
//        System.out.println(list.toString());
//        System.out.println(rows);
    }
}
