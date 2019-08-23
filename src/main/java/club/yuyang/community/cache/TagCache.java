package club.yuyang.community.cache;

import club.yuyang.community.dto.TagDTO;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


/**
 * @author yuyang
 * @date 2019/8/9 9:32
 */
public class TagCache {
    public static List<TagDTO> get(){
        ArrayList<TagDTO> tagDTOS = new ArrayList<>();
        TagDTO program = new TagDTO();
        program.setCategoryName("开发语言");
        program.setTags(Arrays.asList("js","php","java","html","css","node.js","python","c","vue.js","js","php","java","html","css","node.js","python","c","vue.js"));
        tagDTOS.add(program);

        TagDTO framework = new TagDTO();
        framework.setCategoryName("平台框架");
        framework.setTags(Arrays.asList("spring","struts","express","flask","koa"));
        tagDTOS.add(framework);

        TagDTO database = new TagDTO();
        database.setCategoryName("数据库");
        database.setTags(Arrays.asList("mysql","oracle","redis","mongodb","sqlserver"));
        tagDTOS.add(database);

        TagDTO tool = new TagDTO();
        tool.setCategoryName("开发工具");
        tool.setTags(Arrays.asList("idea","eclipse","maven","git","github","visual-studio-code"));
        tagDTOS.add(tool);

        TagDTO other = new TagDTO();
        other.setCategoryName("其他");
        other.setTags(Arrays.asList("其他"));
        tagDTOS.add(other);

        return tagDTOS;
    }

    //校验标签是否合法
    public static String filterInvalid(String tags){
        String[] split = StringUtils.split(tags,',');
        List<TagDTO> tagDTOS = get();

        List<String> tagList = tagDTOS.stream().flatMap(tag -> tag.getTags().stream()).collect(Collectors.toList());
        String invalid = Arrays.stream(split).filter(t -> !tagList.contains(t)).collect(Collectors.joining(","));
        return invalid;
    }
}
