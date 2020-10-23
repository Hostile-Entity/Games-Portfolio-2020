/*
 * File name: MgBullet.cs
 * This file is part of KV-2 Survival game
 * 
 * Created by Iurii Guliev
 * Last modified: Mar 5, 2020
 */

using System.Collections;
using System.Collections.Generic;
using UnityEngine;

//Machine Gun Bullet Class
public class MgBullet : MonoBehaviour
{
    float Speed = 20.0f;

    // Start is called before the first frame update
    void Start()
    {
        Vector3 offset = new Vector3(-0.144f, 0.964f, 0);
        float angle = Vector3.SignedAngle(Vector3.up, transform.up, -Vector3.forward);
        offset = Quaternion.AngleAxis(angle, -Vector3.forward) * offset;
        transform.position = transform.position + offset;

        transform.Rotate(0, 0, Random.Range(-25, 25) / 8.0f);
    }

    // Update is called once per frame
    void Update()
    {
        transform.Translate(Vector3.up * Time.deltaTime * Speed, Space.Self);
        if (!Game.checkBorders(transform.position))
        {
            Destroy(gameObject);
        }
    }

    void OnTriggerEnter2D(Collider2D coll)
    {
        //When
        if (coll.tag == "Enemy")
        {
            Enemy temp = coll.gameObject.GetComponent<Enemy>();
            temp.ApplyDamage(2.01f);
            Destroy(gameObject);
        }
    }
}
