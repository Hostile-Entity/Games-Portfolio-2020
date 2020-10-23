/*
 * File name: CameraScript.cs
 * This file is part of KV-2 Survival game
 * 
 * Created by Iurii Guliev
 * Last modified: Mar 4, 2020
 */

using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.SceneManagement;

public class CameraScript : MonoBehaviour
{

    public GameObject player;

    private Vector3 offset;

    // Start is called before the first frame update
    void Start()
    {
        offset = transform.position - player.transform.position;
    }

    // Update is called once per frame
    void Update()
    {
        if (Input.GetKeyDown(KeyCode.Escape))
        {
            SceneManager.LoadScene("Menu");
        }
    }

    void LateUpdate()
    {
        transform.position = player.transform.position + offset;
    }
}
