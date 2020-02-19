package com.example.demo.controller.adminController;

import com.example.demo.pojo.Type;
import com.example.demo.service.TypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin")
public class TypeController {

    @Autowired
    TypeService typeService;

    //返回分页, 一页7个，按照id倒序排序
    @GetMapping("/types")
    public String list(@PageableDefault(size = 7,
                        sort = {"id"},
                        direction = Sort.Direction.DESC) Pageable pageable,
                        Model model){

        model.addAttribute("page", typeService.listType(pageable));
        return "/admin/types";
    }

    @GetMapping("/types/add")
    public String toAddType(){
        return "/admin/types_input";
    }

    @GetMapping("/types/{id}/update")
    public String toUpdate(@PathVariable Long id, Model model){
        model.addAttribute("type", typeService.getType(id));
        return "/admin/types_input";
    }

    @PostMapping("/types/{id}")
    public String updateType(@PathVariable Long id, Type type, RedirectAttributes rModel){

        if(typeService.getTypeByName(type.getName())!=null){
            rModel.addFlashAttribute("msg", "已存在该分类");
        } else if(type.getName().equals("")){
            rModel.addFlashAttribute("msg", "修改失败");
        }else{
            typeService.updateType(id, type);
            rModel.addFlashAttribute("msg", "修改成功");
        }

        return "redirect:/admin/types";
    }

    @PostMapping("/types/saveType")
    public String addType(Type type, RedirectAttributes rModel){

        if(typeService.getTypeByName(type.getName())!=null){
            rModel.addFlashAttribute("msg", "已存在该分类");
        } else if(type.getName().equals("")){
            rModel.addFlashAttribute("msg", "添加失败");
        }else{
            typeService.save(type);
            rModel.addFlashAttribute("msg", "添加成功");
        }

        return "redirect:/admin/types";
    }

    @GetMapping("/types/{id}/delete")
    public String deleteType(@PathVariable Long id, RedirectAttributes attributes){
        typeService.deleteType(id);
        attributes.addFlashAttribute("msg", "删除成功");

        return "redirect:/admin/types";
    }

}
