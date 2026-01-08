package org.qinyu.vo;

import lombok.Data;
import org.qinyu.entity.Story;

import java.time.LocalDateTime;

@Data
public class SimpleStoryVO {

    private Integer id;

    private String title;

    private LocalDateTime time;

    public SimpleStoryVO(Story story) {
        this.id = story.getId();
        this.title = story.getTitle();
        this.time = story.getTime();
    }
}
