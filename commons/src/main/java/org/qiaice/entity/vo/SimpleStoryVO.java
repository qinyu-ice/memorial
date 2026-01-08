package org.qiaice.entity.vo;

import lombok.Data;
import org.qiaice.entity.table.Story;

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
