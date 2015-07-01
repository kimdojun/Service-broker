package org.openpaas.servicebroker.apiplatform.test;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.UUID;

import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openpaas.servicebroker.common.JsonUtils;
import org.openpaas.servicebroker.common.HttpClientUtils;
import org.openpaas.servicebroker.common.ProvisionBody;
import org.openpaas.servicebroker.exception.ServiceBrokerException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.databind.JsonNode;
import com.sun.org.apache.xml.internal.security.utils.Base64;

/**
 * 시작전에 Spring Boot로 Service Broker를 띄워 놓구 진행합니다.
 * 향후에 Spring Configuration?으로 서비스를 시작하게 만들 예정
 * 
 * @author ahnchan
 */
public class ProvisionRestTest {
	
	private static Properties prop = new Properties();
	
	@BeforeClass
	public static void init() {
		
		System.out.println("== Started test Provision API ==");

		// Initialization
		// Get properties information
		String propFile = "test.properties";
 
		InputStream inputStream = ProvisionRestTest.class.getClassLoader().getResourceAsStream(propFile);
		
		try {
			prop.load(inputStream);
	 	} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
	 		System.err.println(e);
	 	}
		
	}
	
	/**
	 * Provision data Validation
	 * - valid data 
	 */
	@Test	
	public void sendProvision_validData() {
		
		System.out.println("Start - valid data");
		
		HttpHeaders headers = new HttpHeaders();	
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("X-Broker-Api-Version", prop.getProperty("api_version"));
		headers.set("Authorization", "Basic " + new String(Base64.encode((prop.getProperty("auth_id") +":" + prop.getProperty("auth_password")).getBytes())));
		
		String instance_id = UUID.randomUUID().toString();
		String organization_guid = UUID.randomUUID().toString();
		String space_guid = UUID.randomUUID().toString();
		
//		String body = new ProvisionBody(prop.getProperty("provision_service_id"), prop.getProperty("provision_plan_id"), organization_guid, space_guid).convertToJsonString();
		ProvisionBody body = new ProvisionBody(prop.getProperty("provision_service_id"), prop.getProperty("provision_plan_id"), organization_guid, space_guid);
		
//		HttpEntity<String> entity = new HttpEntity<String>(body, headers);		
		HttpEntity<ProvisionBody> entity = new HttpEntity<ProvisionBody>(body, headers);		
		ResponseEntity<String> response = null;

		boolean bException = false;
		
		try {
			
			String url = prop.getProperty("test_base_protocol") + prop.getProperty("test_base_url") + prop.getProperty("provision_path") + "/" + instance_id;
			System.out.println("url:"+url);
			System.out.println("body:"+body);
			
			response = HttpClientUtils.sendProvision(url, entity, HttpMethod.PUT);
			
		} catch (ServiceBrokerException sbe) {
			
			assertFalse("Error", true);
			bException = true;
			
		}
		
		if (!bException) assertTrue("Success", true);
		
		System.out.println("End - valid data");
	}


	/**
	 * Provision data Validation 
	 * - no Service id
	 */
	@Test	
	public void sendProvision_noMandatory_serviceID() {
		
		System.out.println("Start - no mandatory service id");
		
		HttpHeaders headers = new HttpHeaders();	
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("X-Broker-Api-Version", prop.getProperty("api_version"));
		headers.set("Authorization", "Basic " + new String(Base64.encode((prop.getProperty("auth_id") +":" + prop.getProperty("auth_password")).getBytes())));
		
		String instance_id = UUID.randomUUID().toString();
		String organization_guid = UUID.randomUUID().toString();
		String space_guid = UUID.randomUUID().toString();
		
		ProvisionBody body = new ProvisionBody(prop.getProperty("provision_service_id_fail"), prop.getProperty("provision_plan_id"), organization_guid, space_guid);
		
		HttpEntity<ProvisionBody> entity = new HttpEntity<ProvisionBody>(body, headers);		
		ResponseEntity<String> response = null;

		boolean bException = false;
		
		try {
			
			String url = prop.getProperty("test_base_protocol") + prop.getProperty("test_base_url") + prop.getProperty("provision_path") + "/" + instance_id;
			System.out.println("url:"+url);
			System.out.println("body:"+body);
			
			response = HttpClientUtils.sendProvision(url, entity, HttpMethod.PUT);
			
		} catch (ServiceBrokerException sbe) {
			
			assertEquals("ServiceDefinition does not exist: id = "+prop.getProperty("provision_service_id_fail"), sbe.getMessage(), "422 Unprocessable Entity");
			bException = true;
			
		}
		
		if (!bException) assertTrue("Success", true);
		
		System.out.println("End - no mandatory service id");
	}

	/**
	 * Provision data Validation 
	 * - fail Service id
	 */
	@Test	
	public void sendProvision_fail_serviceID() {
		
		System.out.println("Start - fail service id");
		
		HttpHeaders headers = new HttpHeaders();	
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("X-Broker-Api-Version", prop.getProperty("api_version"));
		headers.set("Authorization", "Basic " + new String(Base64.encode((prop.getProperty("auth_id") +":" + prop.getProperty("auth_password")).getBytes())));
		
		String instance_id = UUID.randomUUID().toString();
		String organization_guid = UUID.randomUUID().toString();
		String space_guid = UUID.randomUUID().toString();
		
		ProvisionBody body = new ProvisionBody(prop.getProperty("provision_service_id_fail"), prop.getProperty("provision_plan_id"), organization_guid, space_guid);
		
		HttpEntity<ProvisionBody> entity = new HttpEntity<ProvisionBody>(body, headers);		
		ResponseEntity<String> response = null;

		boolean bException = false;
		
		try {
			
			String url = prop.getProperty("test_base_protocol") + prop.getProperty("test_base_url") + prop.getProperty("provision_path") + "/" + instance_id;
			System.out.println("url:"+url);
			System.out.println("body:"+body);
			
			response = HttpClientUtils.sendProvision(url, entity, HttpMethod.PUT);
			
		} catch (ServiceBrokerException sbe) {
			
			assertTrue("Success", true);
			bException = true;
		}
		
		if (!bException) assertFalse("Success", true);
		
		System.out.println("End - no mandatory service id");
	}
	
	//모든 파라미터가이미 DB에 저장된 것과 같은 값으로 들어온 경우, 200 OK
	@Test	
	public void sendProvision_duplicate_instance() {
		
		System.out.println("Start - duplicate instance id");
		
		HttpHeaders headers = new HttpHeaders();	
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("X-Broker-Api-Version", prop.getProperty("api_version"));
		headers.set("Authorization", "Basic " + new String(Base64.encode((prop.getProperty("auth_id") +":" + prop.getProperty("auth_password")).getBytes())));
		
		String instance_id = prop.getProperty("provison_duplicate_instance_id");
		String organization_guid = prop.getProperty("provison_duplicate_org_id");
		String space_guid = prop.getProperty("provison_duplicate_space_id");
		String service_id= prop.getProperty("provision_duplicate_service_id");
		
		ProvisionBody body = new ProvisionBody(service_id, prop.getProperty("provision__duplicate_plan_id"), organization_guid, space_guid);
		
		HttpEntity<ProvisionBody> entity = new HttpEntity<ProvisionBody>(body, headers);		
		ResponseEntity<String> response = null;

		boolean bException = false;
		
		try {
			
			String url = prop.getProperty("test_base_protocol") + prop.getProperty("test_base_url") + prop.getProperty("provision_path") + "/" + instance_id;
			System.out.println("url:"+url);
			System.out.println("body:"+body);
			
			response = HttpClientUtils.sendProvision(url, entity, HttpMethod.PUT);
			
		} catch (ServiceBrokerException sbe) {
			
			assertEquals("{}", sbe.getMessage(), "500 Internal Server Error");
			bException = true;
		}
		
		if (!bException) assertFalse("Success", true);
		
		System.out.println("End - duplicate instance id");
	}
	
	public void sendProvision_duplicate_instanceID_annotherService() {
		
		System.out.println("Start - duplicate instance id and annother service");
		
		HttpHeaders headers = new HttpHeaders();	
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("X-Broker-Api-Version", prop.getProperty("api_version"));
		headers.set("Authorization", "Basic " + new String(Base64.encode((prop.getProperty("auth_id") +":" + prop.getProperty("auth_password")).getBytes())));
		
		String instance_id = "instance_id";
		String organization_guid = "organizationID";
		String space_guid = "space_id";
		String service_id= prop.getProperty("provision_service_id_annother");
		
		ProvisionBody body = new ProvisionBody(service_id, prop.getProperty("provision_plan_id"), organization_guid, space_guid);
		
		HttpEntity<ProvisionBody> entity = new HttpEntity<ProvisionBody>(body, headers);		
		ResponseEntity<String> response = null;

		boolean bException = false;
		
		try {
			
			String url = prop.getProperty("test_base_protocol") + prop.getProperty("test_base_url") + prop.getProperty("provision_path") + "/" + instance_id;
			System.out.println("url:"+url);
			System.out.println("body:"+body);
			
			response = HttpClientUtils.sendProvision(url, entity, HttpMethod.PUT);
			
		} catch (ServiceBrokerException sbe) {
			
			assertEquals("{}", sbe.getMessage(), "500 Internal Server Error");
			bException = true;
		}
		
		if (!bException) assertFalse("Success", true);
		
		System.out.println("End - duplicate instance id and annother service");
	
	}
	
	
	//200
	//201
	//409
	
	//파라미터 빈값으로 보내서 처리 각각
	//API플랫폼 오류를 체크해서
	//서비스 아이디 없거나 플랜아이디 없는 경우 각각
}
