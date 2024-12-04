package DoAnChuyenNganh.WebsiteChamSocThuCung.controllers;

import DoAnChuyenNganh.WebsiteChamSocThuCung.models.WorkHour;
import DoAnChuyenNganh.WebsiteChamSocThuCung.services.WorkHourService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class WorkHourController {
    @Autowired
    private final WorkHourService workHourService;

    @GetMapping("/workHour/add")
    public String showAddForm(Model model) {
        model.addAttribute("workHour", new WorkHour());
        return "/workHour/add-workHour";
    }

    @PostMapping("/workHour/add")
    public String addWorkHour(@Valid WorkHour workHour, BindingResult result, @RequestParam("time") List<String> timeList) {
        if (result.hasErrors()) {
            return "/workHour/add-workHour";
        }
        for(int i=0;i<timeList.size();i++)
        {
            workHour.setStartTime(timeList.get(i));
            workHourService.saveWorkHour(workHour);
        }


        return "redirect:/workHour";
    }

    @GetMapping("/workHour")
    public String listWorkHour(Model model) {
        List<WorkHour> workHours = workHourService.getAllWorkHour();
        model.addAttribute("workHours", workHours);
        return "/workHour/workHour-list";
    }

    @GetMapping("/workHour/edit/{id}")
    public String showUpdateForm(@PathVariable("id") Long id, Model model) {
        WorkHour workHour = workHourService.getWorkHourById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid workHour Id:"
                        + id));
        model.addAttribute("workHour", workHour);
        return "/workHour/update-workHour";
    }

    @PostMapping("/workHour/update/{id}")
    public String updateWorkHour(@PathVariable("id") Long id, @Valid WorkHour workHour,
                                 BindingResult result, Model model) {
        if (result.hasErrors()) {
            workHour.setId(id);
            return "/workHour/update-workHour";
        }
        workHourService.updateWorkHour(workHour);
        model.addAttribute("workHour", workHourService.getAllWorkHour());
        return "redirect:/workHour";
    }

    @GetMapping("/workHour/delete/{id}")
    public String deleteWorkHour(@PathVariable("id") Long id, Model model) {
        WorkHour workHour = workHourService.getWorkHourById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid workHour Id:"
                        + id));
        workHourService.deleteWorkHour(id);
        model.addAttribute("workHour", workHourService.getAllWorkHour());
        return "redirect:/workHour";
    }
}