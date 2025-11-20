package com.example.demo.apis;

import com.example.demo.cuentacontable.CuentaContable;
import com.example.demo.cuentacontable.CuentaContableRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cuentas")
@RequiredArgsConstructor
@CrossOrigin("*")
public class CuentaApiController {

    private final CuentaContableRepository cuentaRepo;

    @GetMapping("/todas")
    public List<CuentaContable> todas() {
        return cuentaRepo.findAll();
    }
}
