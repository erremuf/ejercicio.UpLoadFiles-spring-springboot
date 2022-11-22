package com.rmf.controllers;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.rmf.models.entity.Cliente;
import com.rmf.models.service.IClienteService;
import com.rmf.util.paginator.PageRender;

@Controller
@SessionAttributes("cliente")
public class ClienteController {

	@Autowired
	private IClienteService clienteService;

	@GetMapping("/listar")
	public String listar(@RequestParam(name = "page", defaultValue = "0") int page, Model model) {

		Pageable pageRequest = PageRequest.of(page, 4);

		Page<Cliente> clientes = clienteService.findAll(pageRequest);

		PageRender<Cliente> pageRender = new PageRender<Cliente>("/listar", clientes);
		model.addAttribute("titulo", "Listado de clientes");
		model.addAttribute("clientes", clientes);
		model.addAttribute("page", pageRender);
		return "listar";
	}

	@GetMapping("/form")
	public String crear(Map<String, Object> model) {

		Cliente cliente = new Cliente();
		model.put("cliente", cliente);
		model.put("titulo", "Formulario de Cliente");
		return "form";
	}

	@GetMapping("/form/{id}")
	public String editar(@PathVariable(value = "id") Long id, Map<String, Object> model, RedirectAttributes flash) {

		Cliente cliente = null;

		if (id > 0) {
			cliente = clienteService.findOne(id);
			if (cliente == null) {
				flash.addFlashAttribute("error", "El ID del cliente no existe en la BBDD!");
				return "redirect:/listar";
			}
		} else {
			flash.addFlashAttribute("error", "El ID del cliente no puede ser cero!");
			return "redirect:/listar";
		}
		model.put("cliente", cliente);
		model.put("titulo", "Editar Cliente");
		return "form";
	}
	
//////////////////////////////PARA TRABAJAR EN LOCAL
	
/*
* @PostMapping("/form") public String guardar(@Valid Cliente cliente,
* BindingResult result, Model model, @RequestParam(name ="file",
* required=false) MultipartFile foto, RedirectAttributes flash, SessionStatus
* status) {
* 
* if (result.hasErrors()) { model.addAttribute("titulo",
* "Formulario de Cliente"); return "form"; }
* 
* // Si foto es distinto a vacio (si hay una foto) if (!foto.isEmpty()) {
* 
* Path directorioRecursos = Paths.get("src/main/resources/static/uploads"); //
* Directorio de subida String rootPath =
* directorioRecursos.toFile().getAbsolutePath();
* 
* try { byte[] bytes = foto.getBytes(); Path rutaCompleta = Paths.get(rootPath
* + "/" + foto.getOriginalFilename()); Files.write(rutaCompleta, bytes); //
* Escritura del archivo flash.addFlashAttribute("info",
* "Has subido correctamente '" + foto.getOriginalFilename() + "'"); // Mensaje
* a cliente
* 
* cliente.setFoto(foto.getOriginalFilename());
* 
* } catch (IOException e) {
* 
* e.printStackTrace(); } }
* 
* String mensajeFlash = (cliente.getId() != null) ?
* "Cliente editado con éxito!" : "Cliente creado con éxito!";
* flash.addFlashAttribute("success", mensajeFlash);
* 
* clienteService.save(cliente);
* 
* status.setComplete();
* 
* return "redirect:/listar"; }
*/
	
	
	
////////////////////////// PARA TRABAJAR EN BBDD
	
	@PostMapping("/form") 	
	public String guardar(@Valid Cliente cliente, BindingResult result, Model model, @RequestParam(name ="file", required=false) MultipartFile foto, RedirectAttributes flash,
			SessionStatus status) {

		if (result.hasErrors()) {
			model.addAttribute("titulo", "Formulario de Cliente");
			return "form";
		}
		
		// Si foto es distinto a vacio (si hay una foto)
		if (!foto.isEmpty()) {
					
			try {	
				
				cliente.setFoto(foto);	
				
			} catch (IOException e) {	
					
				e.printStackTrace();
				
			}
			
			// Mesaje popUP Si edita o si Crea
			String mensajeFlash = (cliente.getId() != null) ? "Cliente editado con éxito!" : "Cliente creado con éxito!";
			flash.addFlashAttribute("success", mensajeFlash);
			
		} else {
				
			// obtenerlo de la BD si existe
			Optional<Cliente> clienteBD = clienteService.findById(cliente.getId() != null ? cliente.getId() : 0);
			
				// si ya existe la persona
				if (clienteBD.isPresent()) {
					
					
				// pero si no existe
				} else {
				
					// poner foto por defecto
					String fileName = "/images/usuario.png";
			
					ClassLoader classLoader = getClass().getClassLoader();
					URL resource = classLoader.getResource(fileName);

					File file2;
			
					try {
						file2 = new File(resource.toURI());
		
						cliente.setFoto(file2);
						
					} catch (Exception e) {
						
						e.printStackTrace();
						
					}
				}																			
		}
		
		clienteService.save(cliente);
		
		status.setComplete();
			
		return "redirect:/listar";
	}
	


	
	
	@GetMapping(value = "/ver/{id}")
	public String ver(@PathVariable(value = "id") Long id, Map<String, Object> model, RedirectAttributes flash) {
		
		Cliente cliente = clienteService.findOne(id);
		
		if (cliente == null) {
			flash.addFlashAttribute("error", "El cliente no existe en la base de datos");
		return "redirect:/listar";
		}
		
		model.put("cliente", cliente);
		model.put("titulo", "Detalle cliente: " + cliente.getNombre());
		
		return "ver";
	}

	@GetMapping("/eliminar/{id}")
	public String eliminar(@PathVariable(value = "id") Long id, RedirectAttributes flash) {

		if (id > 0) {
			clienteService.delete(id);
			flash.addFlashAttribute("success", "Cliente eliminado con éxito!");
		}
		return "redirect:/listar";
	}
}