package com.facetuis.server.service.inviting;

import com.facetuis.server.model.inviting.InvitingImage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class InvitingService {

    public Page<InvitingImage> imageList(){
        InvitingImage image = new InvitingImage();
        image.setUuid("1");
        image.setImg("http://xxx");
        InvitingImage image1 = new InvitingImage();
        image1.setUuid("2");
        image1.setImg("http://xxx");
        InvitingImage image2 = new InvitingImage();
        image2.setUuid("3");
        image2.setImg("http://xxx");
        List<InvitingImage> list = new ArrayList<>();
        list.add(image);
        list.add(image1);
        list.add(image2);
        return  new PageImpl<>(list);
    }
}
