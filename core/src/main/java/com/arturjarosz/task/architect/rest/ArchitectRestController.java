package com.arturjarosz.task.architect.rest;

import com.arturjarosz.task.architect.application.ArchitectApplicationService;
import com.arturjarosz.task.architect.application.dto.ArchitectBasicDto;
import com.arturjarosz.task.architect.application.dto.ArchitectDto;
import com.arturjarosz.task.sharedkernel.utils.HttpHeadersBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("architects")
public class ArchitectRestController {

    private final ArchitectApplicationService architectApplicationService;

    public ArchitectRestController(ArchitectApplicationService architectApplicationService) {
        this.architectApplicationService = architectApplicationService;
    }

    @PostMapping("")
    public ResponseEntity<ArchitectDto> createArchitect(@RequestBody ArchitectBasicDto architectBasicDto) {
        ArchitectDto architectDto = this.architectApplicationService.createArchitect(architectBasicDto);
        HttpHeaders headers = new HttpHeadersBuilder()
                .withLocation("/architects/{id}", architectDto.getId())
                .build();
        return new ResponseEntity<>(architectDto, headers, HttpStatus.CREATED);
    }

    @DeleteMapping("{architectId}")
    public ResponseEntity<Void> removeArchitect(@PathVariable("architectId") Long architectId) {
        this.architectApplicationService.removeArchitect(architectId);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("{architectId}")
    public ResponseEntity<ArchitectDto> getArchitect(@PathVariable("architectId") Long architectId) {
        return new ResponseEntity<>(this.architectApplicationService.getArchitect(architectId),
                HttpStatus.OK);
    }

    @PutMapping("{architectId}")
    public ResponseEntity<ArchitectDto> updateArchitect(@PathVariable("architectId") Long architectId,
                                                        @RequestBody ArchitectDto architectDto) {
        ArchitectDto updatedArchitectDto = this.architectApplicationService.updateArchitect(architectId, architectDto);
        return new ResponseEntity<>(updatedArchitectDto, HttpStatus.OK);
    }

    @GetMapping("")
    public ResponseEntity<List<ArchitectBasicDto>> getBasicArchitects() {
        return new ResponseEntity<>(this.architectApplicationService.getBasicArchitects(),
                HttpStatus.OK);
    }
}
