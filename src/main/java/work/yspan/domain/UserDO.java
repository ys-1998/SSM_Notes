package work.yspan.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.util.List;


/**
 * <p>
 * 
 * </p>
 *
 * @author YS
 * @since 2022-11-25
 */

@TableName("user")
public class UserDO implements Serializable {

    private static final long serialVersionUID = 1L;

      @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 微信openid
     */
    private String openid;

    /**
     * 昵称
     */
    private String name;

    /**
     * 头像
     */
    private String headImg;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 用户签名
     */
    private String sign;

    /**
     * 0表示女，1表示男
     */
    private Integer sex;

    /**
     * 城市
     */
    private String city;

    /**
     * 创建时间
     */
    private Date createTime;

    private List<VideoOrderDO> videoOrderList;

    public List<VideoOrderDO> getVideoOrderList() {
        return videoOrderList;
    }

    public void setVideoOrderList(List<VideoOrderDO> videoOrderList) {
        this.videoOrderList = videoOrderList;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "UserDO{" +
                "id=" + id +
                ", openid='" + openid + '\'' +
                ", name='" + name + '\'' +
                ", headImg='" + headImg + '\'' +
                ", phone='" + phone + '\'' +
                ", sign='" + sign + '\'' +
                ", sex=" + sex +
                ", city='" + city + '\'' +
                ", createTime=" + createTime +
                '}';
    }
}
