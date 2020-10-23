/*
 * File name: Enemy.cs
 * This file is part of KV-2 Survival game
 * 
 * Created by Iurii Guliev
 * Last modified: Mar 4, 2020
 */

using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class Enemy : MonoBehaviour
{
    public GameObject player;
    public GameObject head;
    public Rigidbody2D RigidBody;

    float PowerForward = 550.0f;
    //float PowerBackward = 420.0f;
    float HeadRotationSpeed = 30.0f;
    float BodyRotationSpeed = 50.0f;

    private float HealthPoints = 10.0f;

    // Start is called before the first frame update
    void Start()
    {
        Game.EnemyList.Add(gameObject);
        player = GameObject.FindWithTag("Player");
        
        foreach (var forest in Game.ForestList)
        {
            Physics2D.IgnoreCollision(forest.GetComponent<Collider2D>(), gameObject.GetComponent<Collider2D>());
        }
    }

    // Update is called once per frame
    void Update()
    {
        //Move towards the player
        RigidBody.angularVelocity = 0;
        RigidBody.velocity = transform.up * Vector2.Dot(RigidBody.velocity, transform.up);
        float BodySpeed = RigidBody.velocity.magnitude;
        Vector2 frictionForce = -RigidBody.velocity * RigidBody.mass * Game.STANDART_G * 10 * Time.deltaTime;
        if (BodySpeed > Game.EPS)
        {
            RigidBody.AddForce(frictionForce);
        }
        else
        {
            RigidBody.velocity = new Vector2(0, 0);
        }

        //move
        RigidBody.AddForce(PowerForward * transform.up * 10 * Time.deltaTime);

        //Rotate head
        Vector3 playerDir = player.transform.position - head.transform.position;
        RotateHeadTowards(playerDir);

        //Rotate body
        RotateBodyTowards(playerDir);

        if (HealthPoints < 0)
        {
            Game.AddScore();
            Destroy(gameObject);
        }
    }


    public void RotateHeadTowards(Vector3 direction)
    {
        float angle = Vector3.SignedAngle(direction, head.transform.right, -Vector3.forward);
        if (angle > 0)
        {
            head.transform.Rotate(0, 0, Mathf.Min(HeadRotationSpeed * Time.deltaTime, angle));
        }
        else
        {
            head.transform.Rotate(0, 0, Mathf.Max(-HeadRotationSpeed * Time.deltaTime, angle));
        }
    }

    public void RotateBodyTowards(Vector3 direction)
    {
        float angle = Vector3.SignedAngle(direction, transform.up, -Vector3.forward);
        if (angle > 0)
        {
            transform.Rotate(0, 0, Mathf.Min(BodyRotationSpeed * Time.deltaTime, angle));
        }
        else
        {
            transform.Rotate(0, 0, Mathf.Max(-BodyRotationSpeed * Time.deltaTime, angle));
        }
    }

    void OnCollisionEnter2D(Collision2D coll)
    {
    }

    public void ApplyDamage(float damage)
    {
        HealthPoints -= damage;
    }
}
