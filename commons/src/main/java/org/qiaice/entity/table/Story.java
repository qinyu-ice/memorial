package org.qiaice.entity.table;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Story {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String title;

    private String source;

    private String content;

    private LocalDateTime time;
}
