package com.example.demo.controller;


import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;

import com.example.demo.entity.CategoriesEntity;
import com.example.demo.model.CategoryModel;
import com.example.demo.service.CategoryService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("admin/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("add")
    public String add(Model model) {
        CategoryModel category = new CategoryModel();// Khởi tạo đối tượng CategoryModel mới
        category.setIsEdit(false); // Đặt isEdit thành false để biểu thị đây là thao tác thêm mới
        model.addAttribute("category", category); // Thêm đối tượng vào model để truyền dữ liệu đến view
        return "admin/categories/addOrEdit"; // Trả về view addOrEdit để hiển thị
    }
    @PostMapping("saveOrUpdate")
    public ModelAndView saveOrUpdate(@Valid @ModelAttribute("category") CategoryModel cateModel, BindingResult result, Model model) {
        if(result.hasErrors()) {
            return new ModelAndView("admin/categories/addOrEdit");
        }
        
        CategoriesEntity entity = new CategoriesEntity();
        // Copy từ Model sang Entity
        BeanUtils.copyProperties(cateModel, entity);
        // Gọi hàm save trong service
        categoryService.save(entity);
        // Dữ liệu về cho biến message
        String message = "";
        if(cateModel.isEdit() == true) {
            message = "Category is Edited!!!!!!!!!";
        } else {
            message = "Category is saved!!!!!!!!!";
        }
        
        model.addAttribute("message", message);
        // Redirect về URL controller
        return new ModelAndView("forward:/admin/categories/searchpaginated");
    }


    @RequestMapping("")
    public String list(ModelMap model, @RequestParam(name = "name", required = false) String name) {
        List<CategoriesEntity> list;
        if (name != null && !name.trim().isEmpty()) {
            list = categoryService.findByNameContaining(name);
        } else {
            list = categoryService.findAll();
        }
        model.addAttribute("categories", list);
        model.addAttribute("name", name);
        return "admin/categories/list";
    }

    @GetMapping("edit/{categoryId}")
    public ModelAndView edit(ModelMap model, @PathVariable("categoryId") Long categoryId) {
        Optional<CategoriesEntity> optCategory = categoryService.findById(categoryId);
        CategoryModel cateModel = new CategoryModel();
        // Kiểm tra sự tồn tại của category
        if(optCategory.isPresent()) {
            CategoriesEntity entity = optCategory.get();
            // Copy từ entity sang cateModel
            BeanUtils.copyProperties(entity, cateModel);
            cateModel.setIsEdit(true);
            // Đẩy dữ liệu ra view
            model.addAttribute("category", cateModel);
            return new ModelAndView("admin/categories/addOrEdit", model);
        }

        model.addAttribute("message", "Category is not existed!!!!");
        return new ModelAndView("forward:/admin/categories", model);
    }
     @GetMapping("delete/{categoryId}")
     public ModelAndView delete(ModelMap model, @PathVariable("categoryId") Long categoryId) {
		categoryService.deleteById(categoryId);
		model.addAttribute("message", "Category is deleted!!!!");
		return new ModelAndView("forward:/admin/categories/searchpaginated", model);
}
     @GetMapping("search")
     public String search(ModelMap model, @RequestParam(name = "name", required = false) String name) {
    	    List<CategoriesEntity> list = null;
    	    // Nếu có nội dung truy vấn về name, name là tùy chọn khi required=false
    	    if (StringUtils.hasText(name)) {
    	        list = categoryService.findByNameContaining(name);
    	    } else {
    	        list = categoryService.findAll();
    	    }

    	    model.addAttribute("categories", list);
    	    return "admin/categories/search";
     }
     @RequestMapping("searchpaginated")
     public String searchPaginated(ModelMap model,
                                    @RequestParam(name = "name", required = false) String name,
                                    @RequestParam(name = "page", defaultValue = "1") Optional<Integer> page,
                                    @RequestParam(name = "size", defaultValue = "5") Optional<Integer> size) {

         long count = categoryService.count();
         int currentPage = page.orElse(1);
         int pageSize = size.orElse(5);

         Pageable pageable = PageRequest.of(currentPage - 1, pageSize, Sort.by("name"));
         Page<CategoriesEntity> resultPage = null;

         if (StringUtils.hasText(name)) {
             resultPage = categoryService.findByNameContaining(name, pageable);
             model.addAttribute("name", name);
         } else {
             resultPage = categoryService.findAll(pageable);
         }
         // Defensive: ensure resultPage is never null
         if (resultPage == null) {
             resultPage = Page.empty(pageable);
         }
         int totalPages = resultPage.getTotalPages();
         if (totalPages > 0) {
             int start = Math.max(1, currentPage - 2);
             int end = Math.min(currentPage + 2, totalPages);
             if (end > totalPages) end = totalPages;
             if (start == 1) start = start;
             List<Integer> pageNumbers = IntStream.rangeClosed(start, end)
                     .boxed()
                     .collect(Collectors.toList());
             model.addAttribute("pageNumbers", pageNumbers);
         }

         model.addAttribute("categories", resultPage.getContent());
         model.addAttribute("categoryPage", resultPage);
         return "admin/categories/searchpaginated";
     }
}