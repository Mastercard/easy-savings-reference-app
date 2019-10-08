package com.mastercard.easysavingstutorialapp.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mastercard.api.core.ApiConfig;
import org.openapitools.client.ApiException;
import com.mastercard.api.core.model.RequestMap;
import com.mastercard.api.core.security.oauth.OAuthAuthentication;
import com.mastercard.api.locations.MerchantLocations;
import com.mastercard.easysavingstutorialapp.models.LocationsModel;
import com.mastercard.easysavingstutorialapp.models.LoyaltyOfferModel;
import com.mastercard.easysavingstutorialapp.services.MastercardService;
import org.openapitools.client.ApiClient;
import org.openapitools.client.api.SearchOffersApi;
import org.openapitools.client.model.OfferSearchCriteria;
import org.openapitools.client.model.OfferSearchCriteriaSchema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
public class MastercardController {

    @Value("${mastercard.p12.path}")
    private String p12Path;

    @Value("${mastercard.keystore.alias}")
    private String keyAlias;

    @Value("${mastercard.keystore.pass}")
    private String keyPass;

    @Value("${mastercard.consumer.key}")
    private String consumerKey;

    @Autowired
    private MastercardService mastercardService;

    @RequestMapping("/")
    public String index(Model model) {
        LocationsModel locationsModel = new LocationsModel();
        locationsModel.setCity("New York");
        locationsModel.setPageLength("5");
        locationsModel.setPageOffset("0");

        model.addAttribute("locationsModel", locationsModel);
        model.addAttribute("cities", LocationsModel.getCities());

        return "index.html";
    }

    @GetMapping("/locationsRequest")
    public String locationsRequest(@ModelAttribute("locationsCriteria") LocationsModel locationsModel, RedirectAttributes redirectAttrs) {
    InputStream is;
        try {
            is = new FileInputStream(p12Path);
            ApiConfig.setAuthentication(new OAuthAuthentication(consumerKey, is, keyAlias, keyPass));
            ApiConfig.setSandbox(true);

            RequestMap map = new RequestMap();
            map.set("Details", "offers.easysavings");
            map.set("PageOffset", locationsModel.getPageOffset());
            map.set("PageLength", locationsModel.getPageLength());
            map.set("City", locationsModel.getCity().equals("Any") ? "" : locationsModel.getCity());
            map.set("Latitude", locationsModel.getLatitude());
            map.set("Longitude", locationsModel.getLatitude());
            map.set("Radius", locationsModel.getRadius());

            MerchantLocations locs = MerchantLocations.query(map);
            List<LoyaltyOfferModel> merchants = new ArrayList();
            redirectAttrs.addFlashAttribute("response", new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(locs));

            //Add merchants returned so we can display them in table.
            for(Map<String,Object> item : (List<Map<String, Object>>) locs.get("Merchants.Merchant")) {
                LoyaltyOfferModel merchant = new LoyaltyOfferModel();
                merchant.setMerchantId(item.get("MerchantOfferId").toString());
                merchant.setMerchantName(item.get("Name").toString());
                merchants.add(merchant);
            }
            LoyaltyOfferModel loyaltyOfferModel = new LoyaltyOfferModel();
            loyaltyOfferModel.setMerchantId("");
            loyaltyOfferModel.setMerchantName("");
            redirectAttrs.addFlashAttribute("merchants", merchants);
            redirectAttrs.addFlashAttribute("loyaltyOfferModel", loyaltyOfferModel);

        } catch (com.mastercard.api.core.exception.ApiException | FileNotFoundException | JsonProcessingException e) {
            mastercardService.getErrorAttributes(e, redirectAttrs);
            return "index";
        }

        redirectAttrs.addFlashAttribute("success", "Locations retrieved successfully!");
        return "redirect:/";
    }

    @PostMapping("/loyaltyRequest")
    public String loyaltyRequest(@ModelAttribute("offerSearchCriteria") LoyaltyOfferModel loyaltyModel, RedirectAttributes redirectAttrs) {

        SearchOffersApi api = new SearchOffersApi();
        OfferSearchCriteriaSchema schema = new OfferSearchCriteriaSchema();
        OfferSearchCriteria criteria = new OfferSearchCriteria();
        criteria.setSeoUrl("eId" + loyaltyModel.getMerchantId());
        schema.offerSearchCriteria(criteria);

        try {
            ApiClient client = mastercardService.signRequest();
            api.setApiClient(client);
            redirectAttrs.addFlashAttribute("loyaltyResponse", api.searchPost(schema, "json").toString());
            redirectAttrs.addFlashAttribute("loyaltySuccess", "Offer retrieved successfully!");
        } catch (ApiException e) {
            mastercardService.getErrorAttributes(e, redirectAttrs);
            return "redirect:/";
        }

        return "redirect:/";
    }
}
