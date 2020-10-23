using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class AudioSystem : MonoBehaviour
{
    public AudioSource WinSound;

    public AudioSource Music;

    public AudioSource HitPole;

    public AudioSource HitSound;
    // Start is called before the first frame update

    public static AudioSystem Instance = null;

    private void Awake()
    {
        // if the singleton hasn't been initialized yet
        if (Instance != null && Instance != this)
        {
            Destroy(this.gameObject);
        }
        else
        {
            Instance = this;
        }
        DontDestroyOnLoad(this.gameObject);
    }
    void Start()
    {
        Music.Play();
    }

    // Update is called once per frame
    void Update()
    {
        
    }

    public void playWin()
    {
        WinSound.Play();
    }

    public void playHitPole()
    {
        HitPole.Play();
    }

    public void playHit()
    {
        HitSound.Play();
    }
}
