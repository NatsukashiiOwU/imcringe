package com.bezkoder.spring.security.postgresql.controllers;

import java.io.UnsupportedEncodingException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import com.bezkoder.spring.security.postgresql.models.Cdc;
import com.bezkoder.spring.security.postgresql.repository.CdcRepository;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.bezkoder.spring.security.postgresql.models.ERole;
import com.bezkoder.spring.security.postgresql.models.Role;
import com.bezkoder.spring.security.postgresql.models.User;
import com.bezkoder.spring.security.postgresql.payload.request.LoginRequest;
import com.bezkoder.spring.security.postgresql.payload.request.SignupRequest;
import com.bezkoder.spring.security.postgresql.payload.response.JwtResponse;
import com.bezkoder.spring.security.postgresql.payload.response.MessageResponse;
import com.bezkoder.spring.security.postgresql.repository.RoleRepository;
import com.bezkoder.spring.security.postgresql.repository.UserRepository;
import com.bezkoder.spring.security.postgresql.security.jwt.JwtUtils;
import com.bezkoder.spring.security.postgresql.security.services.UserDetailsImpl;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	UserRepository userRepository;

	@Autowired
	RoleRepository roleRepository;

	@Autowired
	CdcRepository cdcRepository;

	@Autowired
	PasswordEncoder encoder;

	@Autowired
	JwtUtils jwtUtils;

	@Autowired
	private JavaMailSender mailSender;

	@PostMapping("/signin")
		public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

			Authentication authentication = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

			SecurityContextHolder.getContext().setAuthentication(authentication);
			String jwt = jwtUtils.generateJwtToken(authentication);

			UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
			List<String> roles = userDetails.getAuthorities().stream()
					.map(item -> item.getAuthority())
					.collect(Collectors.toList());

		return ResponseEntity.ok(new JwtResponse(jwt, 
												 userDetails.getId(),
												 userDetails.getCdc_id(),
												 userDetails.getUsername(), 
												 userDetails.getEmail(), 
												 roles));
	}

	@PostMapping("/signup")
	public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest, HttpServletRequest request) throws MessagingException, UnsupportedEncodingException {
		if (userRepository.existsByUsername(signUpRequest.getUsername())) {
			return ResponseEntity
					.badRequest()
					.body(new MessageResponse("Error: Username is already taken!"));
		}

		if (userRepository.existsByEmail(signUpRequest.getEmail())) {
			return ResponseEntity
					.badRequest()
					.body(new MessageResponse("Error: Email is already in use!"));
		}

		// Create new user's account
		User user = new User(signUpRequest.getUsername(), 
							 signUpRequest.getEmail());

		Set<String> strRoles = signUpRequest.getRole();
		Set<Role> roles = new HashSet<>();

		if (strRoles == null) {
			Role userRole = roleRepository.findByName(ERole.ROLE_USER)
					.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
			roles.add(userRole);
		} else {
			strRoles.forEach(role -> {
				switch (role) {
				case "admin":
					Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
							.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					roles.add(adminRole);

					break;
				case "mod":
					Role modRole = roleRepository.findByName(ERole.ROLE_MODERATOR)
							.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					roles.add(modRole);

					break;
				default:
					Role userRole = roleRepository.findByName(ERole.ROLE_USER)
							.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					roles.add(userRole);
				}
			});
		}

		if(!cdcRepository.existsByCdcName(signUpRequest.getUniversity())) {
			Cdc newCdc = new Cdc();
			newCdc.setUniversityName(signUpRequest.getUniversity());
			newCdc.setCdcAdminName(user.getUsername());

			cdcRepository.save(newCdc);
		}
		Cdc cdc = cdcRepository.findByCdcUniversityName(signUpRequest.getUniversity());

		user.setRoles(roles);
		user.setCdc_rel(cdc);

		String randomPassword = RandomString.make(8);
		String encodedPassword = encoder.encode(randomPassword);
		user.setPassword(encodedPassword);

		String randomCode = RandomString.make(64);
		user.setVerificationCode(randomCode);
		user.setEnabled(false);

		userRepository.save(user);

		String siteURL = request.getRequestURL().toString();
		siteURL = siteURL.replace(request.getServletPath(), "");
		siteURL += "/api/auth";
		sendVerificationEmailApi(user, siteURL, randomPassword);

		return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
	}

	@GetMapping("/verifyUser")
	public ResponseEntity<?> verifyUser(@Valid @Param("code") String code) {
		if (verify(code)) {
			return ResponseEntity.ok(new MessageResponse("verify successfull!"));
		} else {
			return ResponseEntity.ok(new MessageResponse("verify fail!"));
		}
	}

	public void sendVerificationEmailApi(User user, String siteURL, String randomPassword)
			throws MessagingException, UnsupportedEncodingException {
		String toAddress = user.getEmail();
		String fromAddress = "alpsdi78@gmail.com";
		String senderName = "ThatAnimeKun";
		String subject = "Please verify your registration";
		String content = "Dear [[name]],<br>"
				+ "Here is your password: [[password]]<br>"
				+ "Please click the link below to verify your registration:<br>"
				+ "<h3><a href=\"[[URL]]\" target=\"_self\">VERIFY</a></h3>"
				+ "Thank you,<br>"
				+ "Your company name.";

		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);

		helper.setFrom(fromAddress, senderName);
		helper.setTo(toAddress);
		helper.setSubject(subject);

		content = content.replace("[[name]]", user.getUsername());
		String verifyURL = siteURL + "/verifyUser?code=" + user.getVerificationCode();

		content = content.replace("[[URL]]", verifyURL);

		content = content.replace("[[password]]", randomPassword);

		helper.setText(content, true);

		mailSender.send(message);

		System.out.println("Email has been sent");
	}

	public boolean verify(String verificationCode) {
		User user = userRepository.findByVerificationCode(verificationCode);

		if (user == null || user.isEnabled()) {
			return false;
		} else {
			user.setVerificationCode(null);
			user.setEnabled(true);
			userRepository.save(user);

			return true;
		}

	}

}
