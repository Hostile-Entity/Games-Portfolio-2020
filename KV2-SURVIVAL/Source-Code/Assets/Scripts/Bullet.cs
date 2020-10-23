/*
 * File name: Bullet.cs
 * This file is part of KV-2 Survival game
 * 
 * Created by Iurii Guliev
 * Last modified: Mar 4, 2020
 */

using System.Collections;
using System.Collections.Generic;
using UnityEngine;

//Main Gun Bullet Class
public class Bullet : MonoBehaviour
{
    float Speed = 18.0f;

    // Start is called before the first frame update
    void Start()
    {
        
    }

    // Update is called once per frame
    void Update()
    {
        transform.Translate(Vector3.right * Time.deltaTime * Speed, Space.Self);
        if (!Game.checkBorders(transform.position))
        {
            Destroy(gameObject);
        }
    }

    void OnTriggerEnter2D(Collider2D coll)
    {
        //When the bullet hits enemy, damage enemy
        if (coll.tag == "Enemy")
        {
            Enemy temp = coll.gameObject.GetComponent<Enemy>();
            temp.ApplyDamage(10.01f);
            Destroy(gameObject);
        }
    }
}
