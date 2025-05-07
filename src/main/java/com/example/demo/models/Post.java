package com.example.demo.models;



import com.example.demo.util.EncryptionConverter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Table
@Entity
public class Post {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    @Convert(converter = EncryptionConverter.class)
    private String title;

    @Column
    @Convert(converter = EncryptionConverter.class)
    private String text;

    public void setId(int id) {
        this.id = id;
    }

    @ManyToOne
    @JoinColumn(name = "person_id")
    private Person person;
}
