package com.apirestcrud.primercrudspring.controllers;

import com.apirestcrud.primercrudspring.models.entities.Nota;
import com.apirestcrud.primercrudspring.models.repositories.NotaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class NotaController {

    @Autowired
    private NotaRepository notaRepository;

    @RequestMapping({"/index", "/home", "/", ""})
    public String index(){
        return "index";
    }

    @GetMapping("/notas")
    public List<Nota> listarNotas(){
        return notaRepository.findAll();
    }

    @GetMapping("/notas/{id}")
    public ResponseEntity<?> buscarNota(@PathVariable Long id){
        Nota nota = null;
        Map<String, Object> response = new HashMap<>();

        try {
            nota = notaRepository.findById(id).get();
        }catch(DataAccessException e) {
            response.put("mensaje", "Error al realizar la consulta en la base de datos");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if(nota == null){
            response.put("mensaje", "Error al realizar la consulta en la base de datos");
            response.put("mensaje", "La nota ".concat(id.toString().concat(" no existe en la base de datos.")));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<Nota>(nota, HttpStatus.OK);
    }

    @PostMapping("/notas")
    public ResponseEntity<?> guardarNota(@Valid @RequestBody Nota nota, BindingResult result){
        Nota notaNueva = null;
        Map<String, Object> response = new HashMap<>();

        // proceso de validación
        if(result.hasErrors()) {
            List<String> errors = result.getFieldErrors()
                    .stream()
                    .map(err -> "El campo '" + err.getField() +"' "+ err.getDefaultMessage())
                    .collect(Collectors.toList());
            response.put("errors", errors);
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
        }

        try {
            notaNueva = notaRepository.save(nota);
        } catch(DataAccessException e) {
            response.put("mensaje", "Error al realizar el insert en la base de datos");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("mensaje", "La nota ha sido creada con éxito");
        response.put("nota", notaNueva);

        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
    }

    @PutMapping("/notas/{id}")
    public ResponseEntity<?> editarNota(@Valid @RequestBody Nota nota, BindingResult result, @PathVariable Long id){
        Nota notaActual = notaRepository.findById(id).get();
        Nota notaEditada = null;
        Map<String, Object> response = new HashMap<>();

        // proceso de validación
        if(result.hasErrors()) {
            List<String> errors = result.getFieldErrors()
                    .stream()
                    .map(err -> "El campo '" + err.getField() +"' "+ err.getDefaultMessage())
                    .collect(Collectors.toList());
            response.put("errors", errors);
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
        }

        if(notaActual == null){
            response.put("mensaje", "Error al realizar la consulta en la base de datos");
            response.put("mensaje", "La nota ".concat(id.toString().concat(" no existe en la base de datos.")));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        try {
            notaActual.setTitulo(nota.getTitulo());
            notaActual.setContenido(nota.getContenido());
            notaEditada = notaRepository.save(notaActual);
        } catch(DataAccessException e) {
            response.put("mensaje", "Error al realizar el update en la base de datos");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("mensaje", "La nota ha sido editada con éxito");
        response.put("nota", notaEditada);

        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/notas/{id}")
    public ResponseEntity<?> borrarNota(@PathVariable Long id){
        Nota nota = notaRepository.findById(id).get();
        Map<String, Object> response = new HashMap<>();

        if(nota == null){
            response.put("mensaje", "Error al realizar la consulta en la base de datos");
            response.put("mensaje", "La nota ".concat(id.toString().concat(" no existe en la base de datos.")));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        try {
            notaRepository.deleteById(id);
        }catch(DataAccessException e) {
            response.put("mensaje", "Error al realizar el delete en la base de datos");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("mensaje", "La nota ha sido eliminada con éxito");

        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.ACCEPTED);
    }

}
