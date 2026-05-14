package org.qinyu.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/permission", produces = "application/json; charset=utf-8")
@AllArgsConstructor
public class PermissionController {
}
