/*
 * File name: Menu.cs
 * This file is part of KV-2 Survival game
 * 
 * Created by Iurii Guliev
 * Last modified: Mar 4, 2020
 */

using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.SceneManagement;
using UnityEngine.UI;

public class Menu : MonoBehaviour
{
    public Slider difficultySlider;

    // Start is called before the first frame update
    void Start()
    {
        difficultySlider.value = PlayerPrefs.GetFloat("Difficulty");
    }

    // Update is called once per frame
    void Update()
    {
        if (Input.GetKeyDown(KeyCode.Space))
        {
            Debug.Log("Space");
        }
    }

    public void LoadGame()
    {
        SceneManager.LoadScene("SampleScene");
    }

    public void Quit()
    {
        Application.Quit();
    }

    public void UpdateSlider(float newValue)
    {
        PlayerPrefs.SetFloat("Difficulty", newValue);
    }
}
