package com.sy.sysobackend.common;

import java.io.Serializable;
import lombok.Data;

/**
 * 删除请求
 *
 * @author 诺诺

 */
@Data
public class DeleteRequest implements Serializable {

    /**
     * id
     */
    private Long id;

    private static final long serialVersionUID = 1L;
}