/*
 * File name: Player.cs
 * This file is part of KV-2 Survival game
 * 
 * Created by Iurii Guliev
 * Last modified: Mar 4, 2020
 */

using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.SceneManagement;
using UnityEngine.InputSystem;

public class Player : MonoBehaviour
{
    public GameObject head;
    public GameObject BulletPrefab;
    public GameObject MgBulletPrefab;
    public AudioSource FireShot, IdleSound, MgFireShot;
    public Rigidbody2D RigidBody;

    private Controls controls;
    private PlayerInput input;
    Vector2 movementInput;
    Vector2 headRotationInput;
    float shootGun, shootMg;
    int controlScheme = 1;

    float PowerForward = 850.0f;
    float PowerBackward = 520.0f;
    float RotationSpeed = 60.0f;
    float HeadRotationSpeed = 80.0f;

    float MachineGunReload = 0.1f;
    bool MachineGunReloaded = true;

    float GunReload = 5.0f;
    bool GunReloaded = true;

    Vector3 StartPosition;
    Quaternion StartRotation;


    void Awake()
    {
        controls = new Controls();
        input = FindObjectOfType<PlayerInput>();

        controls.Gameplay.Move.performed += ctx => movementInput = ctx.ReadValue<Vector2>();
        controls.Gameplay.HeadRotation.performed += ctx => headRotationInput = ctx.ReadValue<Vector2>();
        controls.Gameplay.ShootGun.performed += ctx => shootGun = ctx.ReadValue<float>();
        controls.Gameplay.ShootMg.performed += ctx => shootMg = ctx.ReadValue<float>();

    }

    private void OnEnable()
    {
        controls.Gameplay.Enable();
    }

    private void OnDisable()
    {
        controls.Gameplay.Disable();
    }

    // Start is called before the first frame update
    void Start()
    {
        IdleSound.Stop();
        IdleSound.loop = true;
        IdleSound.Play();

        StartPosition = transform.position;
        StartRotation = transform.rotation;
    }

    // Update is called once per frame
    void Update()
    {
        if (input.currentControlScheme.Equals("Keyboard&Mouse"))
        {
            controlScheme = 0;
        }
        else
        {
            controlScheme = 1;
        }
        RigidBody.angularVelocity = 0;
        RigidBody.velocity = transform.up * Vector2.Dot(RigidBody.velocity, transform.up);
        float BodySpeed = RigidBody.velocity.magnitude;
        //Vector2 frictionForce = -RigidBody.velocity.normalized * RigidBody.mass * STANDART_G * 10 * Time.deltaTime;
        Vector2 frictionForce = -RigidBody.velocity * RigidBody.mass * Game.STANDART_G * 10 * Time.deltaTime;
        if (BodySpeed > Game.EPS)
        {
            RigidBody.AddForce(frictionForce);
        } else
        {
            RigidBody.velocity = new Vector2(0, 0);
        }

        //Player controls
        if (movementInput.y > 0)
        {
            RigidBody.AddForce(PowerForward * transform.up * 10 * Time.deltaTime);
        }
        if (movementInput.y < 0)
        {
            RigidBody.AddForce(-PowerBackward * transform.up * 10 * Time.deltaTime);
        }
        if (movementInput.x < 0)
        {
            transform.Rotate(0, 0, RotationSpeed * Time.deltaTime);
        }
        if (movementInput.x > 0)
        {
            transform.Rotate(0, 0, -RotationSpeed * Time.deltaTime);
        }

        //Head rotation
        Debug.Log("Scheme: " + controlScheme);
        if (controlScheme == 0)
        {
            Vector3 mouseWorldPosition = Camera.main.ScreenToWorldPoint(headRotationInput);
            mouseWorldPosition.z = 0.0f;

            Vector3 headDir = mouseWorldPosition - head.transform.position;
            //float angle = Mathf.Atan2(headDir.y, headDir.x) * Mathf.Rad2Deg;
            //head.transform.Rotate(0, 0, angle);
            RotateHeadTowards(headDir);
        } else if (controlScheme == 1)
        {
            if (headRotationInput.x < 0)
            {
                head.transform.Rotate(0, 0, HeadRotationSpeed * Time.deltaTime);
            }
            if (headRotationInput.x > 0)
            {
                head.transform.Rotate(0, 0, -HeadRotationSpeed * Time.deltaTime);
            }
        }

        //shot
        if (shootGun > 0)
        {
            if (GunReloaded)
            {
                Instantiate(BulletPrefab, head.transform.position, head.transform.rotation);
                FireShot.Play();
                GunReloaded = false;
                StartCoroutine(ShootDelay());
            }
        }
        if (shootMg > 0)
        {
            if (MachineGunReloaded)
            {
                Instantiate(MgBulletPrefab, transform.position, transform.rotation);
                MgFireShot.Play();
                MachineGunReloaded = false;
                StartCoroutine(ShootDelayMg());
            }
        }
        /*
        if (MachineGunReloaded == false)
        {
            if (MachineGunTimer > MachineGunReload)
            {
                MachineGunReloaded = true;
                MachineGunTimer = 0;
            } else
            {
                MachineGunTimer += Time.deltaTime;
            }
        }*/
    }


    public void RotateHeadTowards(Vector3 direction)
    {
        float angle = Vector3.SignedAngle(direction, head.transform.right, -Vector3.forward);
        if (angle > 0)
        {
            head.transform.Rotate(0, 0, Mathf.Min(HeadRotationSpeed * Time.deltaTime, angle));
        } else
        {
            head.transform.Rotate(0, 0, Mathf.Max(-HeadRotationSpeed * Time.deltaTime, angle));
        }
    }

    IEnumerator ShootDelay()
    {
        yield return new WaitForSeconds(GunReload);
        GunReloaded = true;
    }

    IEnumerator ShootDelayMg()
    {
        yield return new WaitForSeconds(MachineGunReload);
        MachineGunReloaded = true;
    }

    void OnCollisionEnter2D(Collision2D coll)
    {
        // If the tag of the thing we collide with is "Player"...
        if (coll.gameObject.tag == "Enemy")
        {
            transform.position = StartPosition;
            transform.rotation = StartRotation;
            GunReloaded = true;
            MachineGunReloaded = true;
            RigidBody.velocity = new Vector2(0, 0);

            Game.RestartGame();
        }
    }
}
