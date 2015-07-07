package org.openpaas.servicebroker.cubrid.restTest;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openpaas.servicebroker.common.HttpClientUtils;
import org.openpaas.servicebroker.exception.ServiceBrokerException;
import org.openpaas.servicebroker.util.JsonUtil;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import com.sun.org.apache.xml.internal.security.utils.Base64;

public class updateInstanceRestTest {

	private static Properties prop = new Properties();
	private static String not_maxVol_instance_id = "103cb323-7400-4a87-9a2a-1e245e284c41";
	private static String maxVol_instance_id = "aeb24ef2-9826-4628-9dc9-c919b29f6b58";
	private static String not_exist_instance_id = "7cef84c5-1a3a-4ed6-b9c7-019acbb3f6e4";


	@BeforeClass
	static public void init() {

		System.out.println("== Started test UnProvision API ==");

		// Initialization
		// Get properties information
		String propFile = "test.properties";

		InputStream inputStream = CatalogRestTest.class.getClassLoader().getResourceAsStream(propFile);

		try {
			prop.load(inputStream);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.err.println(e);
		}


		//Provision
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("X-Broker-Api-Version", prop.getProperty("api_version"));
		headers.set("Authorization", "Basic " + new String(Base64.encode((prop.getProperty("auth_id") +":" + prop.getProperty("auth_password")).getBytes())));

		String organization_guid = UUID.randomUUID().toString();
		String space_guid = UUID.randomUUID().toString();

		Map<String, String> bodyMap = new HashMap<String,String>();

		bodyMap.put("service_id", "cubrid");
		bodyMap.put("plan_id", "100mb-utf8");
		bodyMap.put("organization_guid", organization_guid);
		bodyMap.put("space_guid", space_guid);

		HttpEntity<String> entity = new HttpEntity<String>(JsonUtil.convertToJson(bodyMap), headers);
		ResponseEntity<String> response = null;

		try {
			String APIendpoint = prop.getProperty("test_base_protocol") + prop.getProperty("test_base_url");
			String url = APIendpoint + "/v2/service_instances/" + not_maxVol_instance_id;

			response = HttpClientUtils.send(url, entity, HttpMethod.PUT);

			if (response.getStatusCode() != HttpStatus.CREATED) throw new ServiceBrokerException("Response code is " + response.getStatusCode());

		} catch (ServiceBrokerException sbe) {

		}

		bodyMap.remove("plan_id");
		bodyMap.put("plan_id", "200mb-utf8");
		entity = new HttpEntity<String>(JsonUtil.convertToJson(bodyMap), headers);
		response = null;
		
		try {
			String APIendpoint = prop.getProperty("test_base_protocol") + prop.getProperty("test_base_url");
			String url = APIendpoint + "/v2/service_instances/" + maxVol_instance_id;

			response = HttpClientUtils.send(url, entity, HttpMethod.PUT);

			if (response.getStatusCode() != HttpStatus.CREATED) throw new ServiceBrokerException("Response code is " + response.getStatusCode());

		} catch (ServiceBrokerException sbe) {

		}

	}

	@AfterClass
	static public void closer() {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("X-Broker-Api-Version", prop.getProperty("api_version"));
		headers.set("Authorization", "Basic " + new String(Base64.encode((prop.getProperty("auth_id") +":" + prop.getProperty("auth_password")).getBytes())));

		HttpEntity<String> entity = new HttpEntity<String>("", headers);
		ResponseEntity<String> response = null;

		try {
			String APIendpoint = prop.getProperty("test_base_protocol") + prop.getProperty("test_base_url");
			String url = APIendpoint + "/v2/service_instances/" + not_maxVol_instance_id + "?service_id=&plan_id=";

			response = HttpClientUtils.send(url, entity, HttpMethod.DELETE);

			if (response.getStatusCode() != HttpStatus.OK) throw new ServiceBrokerException("Response code is " + response.getStatusCode());

		} catch (ServiceBrokerException sbe) {

		}

		try {
			String APIendpoint = prop.getProperty("test_base_protocol") + prop.getProperty("test_base_url");
			String url = APIendpoint + "/v2/service_instances/" + maxVol_instance_id + "?service_id=&plan_id=";

			response = HttpClientUtils.send(url, entity, HttpMethod.DELETE);

			if (response.getStatusCode() != HttpStatus.OK) throw new ServiceBrokerException("Response code is " + response.getStatusCode());

		} catch (ServiceBrokerException sbe) {

		}

		System.out.println("== End test UnProvision API ==");

	}

	@Test
	public void updateInstanceTest_200OK() {

		System.out.println("Start - add volume. volume is not max.");

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("X-Broker-Api-Version", prop.getProperty("api_version"));
		headers.set("Authorization", "Basic " + new String(Base64.encode((prop.getProperty("auth_id") +":" + prop.getProperty("auth_password")).getBytes())));

		Map<String, String> bodyMap = new HashMap<String,String>();

		bodyMap.put("plan_id", "200mb-utf8");

		HttpEntity<String> entity = new HttpEntity<String>(JsonUtil.convertToJson(bodyMap), headers);
		ResponseEntity<String> response = null;

		try {
			String APIendpoint = prop.getProperty("test_base_protocol") + prop.getProperty("test_base_url");
			String url = APIendpoint + "/v2/service_instances/" + not_maxVol_instance_id;

			response = HttpClientUtils.send(url, entity, HttpMethod.PATCH);

		} catch (ServiceBrokerException sbe) {

			System.out.println(sbe.getMessage());

		} 

		assertEquals(HttpStatus.OK, response.getStatusCode());
		System.out.println("End - add volume. volume is not max.");

	}

	@Test
	public void updateInstanceTest_500InternalServerError() {

		System.out.println("Start - add volume. not exist instance_id");

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("X-Broker-Api-Version", prop.getProperty("api_version"));
		headers.set("Authorization", "Basic " + new String(Base64.encode((prop.getProperty("auth_id") +":" + prop.getProperty("auth_password")).getBytes())));

		Map<String, String> bodyMap = new HashMap<String,String>();

		bodyMap.put("plan_id", "200mb-utf8");

		HttpEntity<String> entity = new HttpEntity<String>(JsonUtil.convertToJson(bodyMap), headers);
		ResponseEntity<String> response = null;

		boolean bException = false;

		try {
			String APIendpoint = prop.getProperty("test_base_protocol") + prop.getProperty("test_base_url");
			String url = APIendpoint + "/v2/service_instances/" + not_exist_instance_id;

			response = HttpClientUtils.send(url, entity, HttpMethod.PATCH);

		} catch (ServiceBrokerException sbe) {

			System.out.println(sbe.getMessage());
			assertEquals("500 Internal Server Error", sbe.getMessage());
			bException = true;

		} 

		if (!bException) assertFalse("Success", true);

		System.out.println("End - add volume. not exist instance_id");

	}

	@Test
	public void updateInstanceTest_422_maxVol_instance() {

		System.out.println("Start - add volume. volume is not max.");

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("X-Broker-Api-Version", prop.getProperty("api_version"));
		headers.set("Authorization", "Basic " + new String(Base64.encode((prop.getProperty("auth_id") +":" + prop.getProperty("auth_password")).getBytes())));

		Map<String, String> bodyMap = new HashMap<String,String>();

		bodyMap.put("plan_id", "200mb-utf8");

		HttpEntity<String> entity = new HttpEntity<String>(JsonUtil.convertToJson(bodyMap), headers);
		ResponseEntity<String> response = null;

		boolean bException = false;

		try {
			String APIendpoint = prop.getProperty("test_base_protocol") + prop.getProperty("test_base_url");
			String url = APIendpoint + "/v2/service_instances/" + maxVol_instance_id;

			response = HttpClientUtils.send(url, entity, HttpMethod.PATCH);
			
			if (response.getStatusCode() != HttpStatus.OK) throw new ServiceBrokerException("Response code is " + response.getStatusCode());

		} catch (ServiceBrokerException sbe) {

			System.out.println(sbe.getMessage());
			assertEquals("422 Unprocessable Entity", sbe.getMessage());
			bException = true;

		} 

		if (!bException) assertFalse("Success", true);

		System.out.println("End - add volume. volume is not max.");

	}

	@Test
	public void updateInstanceTest_422_charset() {

		System.out.println("Start - add volume. volume is not max.");

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("X-Broker-Api-Version", prop.getProperty("api_version"));
		headers.set("Authorization", "Basic " + new String(Base64.encode((prop.getProperty("auth_id") +":" + prop.getProperty("auth_password")).getBytes())));

		Map<String, String> bodyMap = new HashMap<String,String>();

		bodyMap.put("plan_id", "200mb-euctk");

		HttpEntity<String> entity = new HttpEntity<String>(JsonUtil.convertToJson(bodyMap), headers);
		ResponseEntity<String> response = null;

		boolean bException = false;

		try {
			String APIendpoint = prop.getProperty("test_base_protocol") + prop.getProperty("test_base_url");
			String url = APIendpoint + "/v2/service_instances/" + maxVol_instance_id;

			response = HttpClientUtils.send(url, entity, HttpMethod.PATCH);

			if (response.getStatusCode() != HttpStatus.OK) throw new ServiceBrokerException("Response code is " + response.getStatusCode());
			
		} catch (ServiceBrokerException sbe) {

			System.out.println(sbe.getMessage());
			assertEquals("422 Unprocessable Entity", sbe.getMessage());
			bException = true;

		} 

		if (!bException) assertFalse("Success", true);

		System.out.println("End - add volume. volume is not max.");

	}

}
